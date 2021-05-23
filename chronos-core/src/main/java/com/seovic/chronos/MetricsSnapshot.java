package com.seovic.chronos;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Point-in-time snapshot of test results.
 *
 * @author Aleksandar Seovic
 */
public class MetricsSnapshot
    {
    private String testName;
    private long timestamp;
    private Map<String, TimerSnapshot> timers;

    public MetricsSnapshot(String testName, MetricRegistry metrics)
        {
        this.testName = testName;
        this.timestamp = System.currentTimeMillis();

        SortedMap<String, TimerSnapshot> timers = new TreeMap<>();
        for (Map.Entry<String, Timer> timer : metrics.getTimers().entrySet())
            {
            timers.put(timer.getKey(), new TimerSnapshot(timer.getValue()));
            }

        this.timers = timers;
        }

    public long getTimestamp()
        {
        return timestamp;
        }

    public String getTestName()
        {
        return testName;
        }

    public double getThroughput()
        {
        return timers.values().stream().mapToDouble(TimerSnapshot::getThroughput).sum();
        }

    public double getDuration()
        {
        return getOps() / getThroughput();
        }

    public long getOps()
        {
        return timers.values().stream().mapToLong(TimerSnapshot::getCount).sum();
        }

    public Map<String, TimerSnapshot> getTimers()
        {
        return timers;
        }

    public String toString()
        {
        return String.format("\n%s:" +
               "\n\t duration:   %,.2f sec" +
               "\n\t ops:        %,d ops" +
               "\n\t throughput: %,.0f ops/sec" +
               "\n\t timers: %s", testName, getDuration(), getOps(), getThroughput(), timersToString());
        }

    private String timersToString()
        {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TimerSnapshot> ts : timers.entrySet())
            {
            sb.append("\n\t\t ")
              .append(ts.getKey())
              .append(": ")
              .append(ts.getValue());
            }
        return sb.toString();
        }
    }
