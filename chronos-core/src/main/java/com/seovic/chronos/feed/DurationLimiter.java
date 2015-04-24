package com.seovic.chronos.feed;


import com.seovic.chronos.Request;
import com.seovic.chronos.RequestFeed;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Limits duration (in milliseconds) of the underlying feed.
 *
 * @author Aleksandar Seovic  2015.04.24
 */
public class DurationLimiter
        implements RequestFeed
    {
    private volatile long start;

    private final RequestFeed feed;
    private final long limit;

    /**
     * Construct DurationLimiter instance.
     *
     * @param feed   the RequestFeed to wrap
     * @param limit  the number of milliseconds to limit the request generation to
     */
    public DurationLimiter(RequestFeed feed, long limit)
        {
        this.feed  = feed;
        this.limit = limit;
        }

    @Override
    public Request next()
        {
        if (start == 0)
            {
            start = System.currentTimeMillis();
            }

        long t = System.currentTimeMillis();
        return t - start > limit ? null : feed.next();
        }
    }
