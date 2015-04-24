package com.seovic.chronos.feed;


import com.seovic.chronos.Request;
import com.seovic.chronos.RequestFeed;

import java.util.concurrent.atomic.AtomicLong;


/**
 * @author Aleksandar Seovic  2015.04.24
 */
public class IterationLimiter implements RequestFeed
    {
    private AtomicLong count = new AtomicLong();

    private final RequestFeed feed;
    private final long limit;

    public IterationLimiter(RequestFeed feed, long limit)
        {
        this.feed  = feed;
        this.limit = limit;
        }

    public Request next()
        {
        long c = count.incrementAndGet();
        return c > limit ? null : feed.next();
        }
    }
