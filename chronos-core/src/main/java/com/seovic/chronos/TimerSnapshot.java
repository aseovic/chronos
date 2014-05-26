package com.seovic.chronos;


import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;


/**
 * @author Aleksandar Seovic  2014.05.25
 */
public class TimerSnapshot
    {
    private long count;
    private double oneMinuteRate;
    private double fiveMinuteRate;
    private double fifteenMinuteRate;
    private double meanRate;
    private Snapshot snapshot;


    public TimerSnapshot(Timer timer)
        {
        count = timer.getCount();
        oneMinuteRate = timer.getOneMinuteRate();
        fiveMinuteRate = timer.getFiveMinuteRate();
        fifteenMinuteRate = timer.getFifteenMinuteRate();
        meanRate = timer.getMeanRate();
        snapshot = timer.getSnapshot();
        }

    public long getCount()
        {
        return count;
        }

    public double getOneMinuteRate()
        {
        return oneMinuteRate;
        }

    public double getFiveMinuteRate()
        {
        return fiveMinuteRate;
        }

    public double getFifteenMinuteRate()
        {
        return fifteenMinuteRate;
        }

    public double getMeanRate()
        {
        return meanRate;
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
        StringBuilder sb = new StringBuilder();
        sb.append("count=").append(getCount()).append(", ");
        sb.append("median=").append(snapshot.getMedian()).append(", ");
        sb.append("75%=").append(snapshot.get75thPercentile()).append(", ");
        sb.append("95%=").append(snapshot.get95thPercentile()).append(", ");
        sb.append("98%=").append(snapshot.get98thPercentile()).append(", ");
        sb.append("99%=").append(snapshot.get99thPercentile()).append(", ");
        sb.append("99.9%=").append(snapshot.get999thPercentile()).append(", ");
        sb.append("max=").append(snapshot.getMax()).append(", ");
        sb.append("min=").append(snapshot.getMin()).append(", ");
        sb.append("mean=").append(snapshot.getMean()).append(", ");
        sb.append("std dev=").append(snapshot.getStdDev()).append(", ");
        sb.append("mean rate=").append(getMeanRate()).append(", ");
        sb.append("1 min rate=").append(getOneMinuteRate()).append(", ");
        sb.append("5 min rate=").append(getFiveMinuteRate()).append(", ");
        sb.append("15 min rate=").append(getFifteenMinuteRate());
        return sb.toString();
        }

    }
