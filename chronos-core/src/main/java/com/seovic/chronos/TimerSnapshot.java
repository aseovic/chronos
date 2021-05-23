package com.seovic.chronos;

import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author Aleksandar Seovic  2014.05.25
 */
public class TimerSnapshot
    {
    private long count;

    private double throughput;

    private Snapshot snapshot;

    public TimerSnapshot(Timer timer)
        {
        count = timer.getCount();
        throughput = timer.getMeanRate();
        snapshot = timer.getSnapshot();
        }

    public long getCount()
        {
        return count;
        }

    public double getThroughput()
        {
        return throughput;
        }

    public double getMedian()
        {
        return snapshot.getMedian();
        }

    public double get75thPercentile()
        {
        return snapshot.get75thPercentile();
        }

    public double get95thPercentile()
        {
        return snapshot.get95thPercentile();
        }

    public double get98thPercentile()
        {
        return snapshot.get98thPercentile();
        }

    public double get99thPercentile()
        {
        return snapshot.get99thPercentile();
        }

    public double get999thPercentile()
        {
        return snapshot.get999thPercentile();
        }

    public long getMax()
        {
        return snapshot.getMax();
        }

    public long getMin()
        {
        return snapshot.getMin();
        }

    public double getMean()
        {
        return snapshot.getMean();
        }

    public double getStdDev()
        {
        return snapshot.getStdDev();
        }

    public String toString()
        {
        return toString(TimeUnit.MILLISECONDS);
        }

    public String toString(TimeUnit unit)
        {
        return "count=" + String.format("%,d", getCount())
               + " throughput=" + String.format("%,.0f", getThroughput())
               + " min=" + convert(snapshot.getMin(), unit)
               + " max=" + convert(snapshot.getMax(), unit)
               + " avg=" + convert(snapshot.getMean(), unit)
               + " std=" + convert(snapshot.getStdDev(), unit)
               + " 50%=" + convert(snapshot.getMedian(), unit)
               + " 75%=" + convert(snapshot.get75thPercentile(), unit)
               + " 95%=" + convert(snapshot.get95thPercentile(), unit)
               + " 98%=" + convert(snapshot.get98thPercentile(), unit)
               + " 99%=" + convert(snapshot.get99thPercentile(), unit)
               + " 99.9%=" + convert(snapshot.get999thPercentile(), unit);
        }

    private String convert(double nanos, TimeUnit unit)
        {
        switch (unit)
            {
            case NANOSECONDS:
                return String.format("%,.0fns", nanos);
            case MICROSECONDS:
                return String.format("%,.3fus", nanos / 1_000);
            case MILLISECONDS:
                return String.format("%,.3fms", nanos / 1_000_000);
            case SECONDS:
                return String.format("%,.3fs", nanos / 1_000_000_000);
            default:
                throw new IllegalArgumentException("Only ns, us, ms and s are supported");
            }
        }
    }
