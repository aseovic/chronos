package com.seovic.chronos;

import com.codahale.metrics.Timer;
import com.seovic.chronos.util.TimeUtil;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * Limits the number of requests each thread will execute in a given amount of time.
 *
 * @author Aleksandar Seovic
 */
public class RateLimiter {
    private long requests;
    private long perMillis;

    /**
     * Create RateLimiter instance.
     *
     * @param requests     the number of requests to limit the rate to
     * @param perDuration  the duration that the number of request applies to (typically 1)
     * @param perUnit      the time unit that the number of request applies to (SECONDS, MINUTES, HOURS or DAYS)
     */
    public RateLimiter(long requests, long perDuration, TemporalUnit perUnit) {
        this.requests = requests;
        this.perMillis = Duration.of(perDuration, perUnit).toMillis();
    }

    /**
     * Sleeps for a period of time as necessary in order to limit request rate.
     *
     * @param timer  statistics used to calculate delay time for next request
     */
    public void throttle(Timer timer) throws InterruptedException {
        long millisPerRequest = perMillis / requests;
        long delay = millisPerRequest - Math.round(timer.getSnapshot().getMean());
        if (delay > 0) {
            Thread.sleep(delay);
        }
    }
}
