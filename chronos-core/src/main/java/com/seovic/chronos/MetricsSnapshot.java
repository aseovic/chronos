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
    private long timestamp;
    private Map<String, TimerSnapshot> timers;

    public MetricsSnapshot(MetricRegistry metrics)
        {
        this.timestamp = System.currentTimeMillis();

        SortedMap<String, TimerSnapshot> timers = new TreeMap<String, TimerSnapshot>();
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

    public Map<String, TimerSnapshot> getTimers()
        {
        return timers;
        }
    }
