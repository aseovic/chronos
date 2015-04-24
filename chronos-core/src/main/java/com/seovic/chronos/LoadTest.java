package com.seovic.chronos;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import com.seovic.chronos.feed.DurationLimiter;
import com.seovic.chronos.feed.IterationLimiter;
import com.seovic.chronos.feed.SingletonFeed;
import com.seovic.chronos.util.TimeUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import java.util.concurrent.*;


/**
 * Base class for load tests.
 *
 * @author Aleksandar Seovic
 */
@SuppressWarnings("unchecked")
public class LoadTest
    {
    private String name;
    private int threadCount = 1;
    private Set<Publisher> publishers = new LinkedHashSet<Publisher>();
    private long publishInterval = 5000;
    private long runLimit = 0;
    private long timeLimit = 0;
    private RateLimiter rateLimiter;
    private RequestFeedFactory feedFactory;
    private final MetricRegistry metrics = new MetricRegistry();

    public LoadTest(String name)
        {
        this.name = name;
        }

    /**
     * Set the number of test threads to run.
     * <p/>
     * Each test thread will be configured with an instance of each own {@link
     * RequestFeed} and will terminate once the request feed is fully consumed.
     *
     * @param threadCount the number of test threads to create
     *
     * @return this test
     */
    public LoadTest withThreadCount(int threadCount)
        {
        this.threadCount = threadCount;
        return this;
        }

    /**
     * Add result {@link Publisher}.
     *
     * @param publisher publisher to add
     *
     * @return this test
     */
    public LoadTest withPublisher(Publisher publisher)
        {
        publishers.add(publisher);
        return this;
        }

    /**
     * Set publish interval (the default is 5 seconds).
     *
     * @param interval the interval for test results publication
     * @param unit     time unit for the publication interval
     *
     * @return this test
     */
    public LoadTest withPublishInterval(int interval, TimeUnit unit)
        {
        if (interval <= 0)
            {
            throw new IllegalArgumentException("Publish interval must be a positive integer");
            }
        publishInterval = TimeUtil.toMillis(interval, unit);
        return this;
        }

    /**
     * Set rate limit for this test.
     *
     * @param requests the number of requests per thread to limit the rate to
     * @param perUnit  the time unit that the number of request applies to
     *                 (SECONDS, MINUTES, HOURS or DAYS)
     *
     * @return this test
     */
    public LoadTest withRateLimit(long requests, TimeUnit perUnit)
        {
        this.rateLimiter = new RateLimiter(requests, 1, perUnit);
        return this;
        }

    public LoadTest withRequest(final Request request)
        {
        feedFactory = new RequestFeedFactory()
            {
            @Override
            public RequestFeed create(int threadNumber, int threadCount)
                {
                try
                    {
                    return new SingletonFeed(request);
                    }
                catch (Exception e)
                    {
                    throw new RuntimeException(e);
                    }
                }
            };

        return this;
        }

    public LoadTest withRequestFeed(final RequestFeed feed)
        {
        feedFactory = new RequestFeedFactory()
            {
            @Override
            public RequestFeed create(int threadNumber, int threadCount)
                {
                try
                    {
                    return feed;
                    }
                catch (Exception e)
                    {
                    throw new RuntimeException(e);
                    }
                }
            };

        return this;
        }

    public LoadTest withRequestFeedFactory(final RequestFeedFactory feedFactory)
        {
        this.feedFactory = feedFactory;
        return this;
        }

    public MetricsSnapshot runFor(final long iterations)
        {
        if (feedFactory == null)
            {
            throw new IllegalStateException("Request feed factory must be configured");
            }

        final RequestFeedFactory factory = feedFactory;
        feedFactory = new RequestFeedFactory()
            {
            public RequestFeed create(int threadNumber, int threadCount)
                {
                return new IterationLimiter(factory.create(threadNumber, threadCount), iterations);
                }
            };

        return run();
        }

    public MetricsSnapshot runFor(int duration, TimeUnit unit)
        {
        if (feedFactory == null)
            {
            throw new IllegalStateException("Request feed factory must be configured");
            }

        final long durationMillis = TimeUtil.toMillis(duration, unit);
        final RequestFeedFactory factory = feedFactory;

        feedFactory = new RequestFeedFactory()
            {
            public RequestFeed create(int threadNumber, int threadCount)
                {
                return new DurationLimiter(factory.create(threadNumber, threadCount), durationMillis);
                }
            };

        return run();
        }

    protected MetricsSnapshot run()
        {
        RequestFeed[] feeds = new RequestFeed[threadCount];
        for (int i = 0; i < threadCount; i++)
            {
            feeds[i] = feedFactory.create(i, threadCount);
            }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++)
            {
            executor.submit(new RequestExecutor(feeds[i], latch));
            }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture publisherDaemon = scheduler.scheduleAtFixedRate(
                new MetricsPublisher(), publishInterval, publishInterval, TimeUnit.MILLISECONDS);

        try
            {
            latch.await();
            }
        catch (InterruptedException ignore)
            {
            }

        publisherDaemon.cancel(false);
        scheduler.shutdown();

        return new MetricsSnapshot(metrics);
        }

    private class RequestExecutor
            implements Runnable
        {
        private final RequestFeed feed;
        private final CountDownLatch latch;
        private boolean interrupted;

        public RequestExecutor(RequestFeed feed, CountDownLatch latch)
            {
            this.feed = feed;
            this.latch = latch;
            }

        @Override
        public void run()
            {
            Request request;
            while (!interrupted && (request = feed.next()) != null)
                {
                Timer tSuccess = metrics.timer(name + "." + request.getName() + ".success");
                Timer tFailure = metrics.timer(name + "." + request.getName() + ".failure");
                try
                    {
                    long start = System.nanoTime();
                    boolean success = request.execute();
                    long duration = System.nanoTime() - start;
                    if (success)
                        {
                        tSuccess.update(duration, TimeUnit.NANOSECONDS);
                        }
                    else
                        {
                        tFailure.update(duration, TimeUnit.NANOSECONDS);
                        }

                    if (rateLimiter != null)
                        {
                        rateLimiter.throttle(success ? tSuccess : tFailure);
                        }

                    interrupted = Thread.currentThread().isInterrupted();
                    }
                catch (InterruptedException e)
                    {
                    interrupted = true;
                    }
                }
            latch.countDown();
            }
        }

    private class MetricsPublisher
            implements Runnable
        {
        @Override
        public void run()
            {
            MetricsSnapshot snapshot = new MetricsSnapshot(metrics);
            for (Publisher pub : publishers)
                {
                pub.publish(snapshot);
                }
            }
        }
    }
