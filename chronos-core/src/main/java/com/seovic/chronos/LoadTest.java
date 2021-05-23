package com.seovic.chronos;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import com.seovic.chronos.feed.DurationLimiter;
import com.seovic.chronos.feed.IterationLimiter;
import com.seovic.chronos.feed.SingletonFeed;

import com.seovic.chronos.publisher.CsvPublisher;
import com.seovic.chronos.publisher.GraphitePublisher;
import com.seovic.chronos.publisher.PrinterPublisher;

import java.io.File;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.*;

import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Base class for load tests.
 *
 * @author Aleksandar Seovic
 */
@SuppressWarnings("unchecked")
public class LoadTest<T extends LoadTest<T>> implements Runnable
    {
    private String name;

    @Option(names = {"-t", "--threads"}, description = "The number of concurrent threads to run")
    private int threadCount = 1;

    @Option(names = {"-d", "--duration"}, description = "The duration to run the test for, in seconds or in ISO-8601 format", converter = DurationConverter.class)
    private Duration duration;

    @Option(names = {"-c", "--request-count"}, description = "The number of requests (per thread) to run the test for")
    private long requestCount;

    @Option(names = {"-i", "--publish-interval"}, description = "The interval to publish the results at to all registered publishers")
    private long publishInterval;

    @Option(names = "--csv", description = "The name of the file to publish the results to in CSV format")
    private File csvOutput;

    @Option(names = "--graphite", description = "The host:port of the Graphite server to publish the results to")
    private String graphiteHost;

    @Option(names = "--out", description = "The name of the file to publish the results to in standard format")
    private File standardOutput;

    private final Set<Publisher> publishers = new LinkedHashSet<>();

    private RateLimiter rateLimiter;
    private RequestFeedFactory feedFactory;
    private MetricRegistry metrics;

    protected LoadTest()
        {
        name = getClass().getName();
        }

    public LoadTest(String name)
        {
        this.name = name;
        }

    /**
     * Set the name of the test.
     *
     * @param name the name of the test
     *
     * @return this test
     */
    public T withName(String name)
        {
        this.name = name;
        return (T) this;
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
    public T withThreadCount(int threadCount)
        {
        this.threadCount = threadCount;
        return (T) this;
        }

    /**
     * Add result {@link Publisher}.
     *
     * @param publisher publisher to add
     *
     * @return this test
     */
    public T withPublisher(Publisher publisher)
        {
        publishers.add(publisher);
        return (T) this;
        }

    /**
     * Set publish interval (the default is 5 seconds).
     *
     * @param interval the interval for test results publication
     * @param unit     time unit for the publication interval
     *
     * @return this test
     */
    public T withPublishInterval(long interval, TemporalUnit unit)
        {
        if (interval <= 0)
            {
            throw new IllegalArgumentException("Publish interval must be a positive integer");
            }
        publishInterval = Duration.of(interval, unit).toMillis();
        return (T) this;
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
    public T withRateLimit(long requests, TemporalUnit perUnit)
        {
        this.rateLimiter = new RateLimiter(requests, 1L, perUnit);
        return (T) this;
        }

    public T withRequest(final Request request)
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

        return (T) this;
        }

    public T withRequestFeed(final RequestFeed feed)
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

        return (T) this;
        }

    public T withRequestFeedFactory(final RequestFeedFactory feedFactory)
        {
        this.feedFactory = feedFactory;
        return (T) this;
        }

    /**
     * Runs the test using {@code picocli} command line utility.
     */
    public void run()
        {
        // configure publishers to report the results to
        publishers.add(new PrinterPublisher());  // always print results to console
        if (standardOutput != null)
            {
            publishers.add(new PrinterPublisher(standardOutput));
            }
        if (csvOutput != null)
            {
            publishers.add(new CsvPublisher(csvOutput));
            }
        if (graphiteHost != null)
            {
            publishers.add(new GraphitePublisher(graphiteHost));
            }

        // run the test either based on the configured duration or the number of requests
        if (duration != null)
            {
            runFor(duration);
            }
        else if (requestCount > 0)
            {
            runFor(requestCount);
            }
        else
            {
            System.out.println("Either the run duration (-d) or the number od requests to make (-c) must be specified");
            System.exit(1);
            }

        new MetricsPublisher().run();
        }

    public MetricsSnapshot runFor(long iterations)
        {
        System.out.printf("\nRunning %s for %,d requests from %d client threads...\n", name, iterations * threadCount, threadCount);
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

        MetricsSnapshot snapshot = runTest();
        feedFactory = factory;

        return snapshot;
        }

    public MetricsSnapshot runFor(Duration duration)
        {
        System.out.printf("\nRunning %s for %,d seconds from %d client threads...\n", name, duration.toSeconds(), threadCount);
        if (feedFactory == null)
            {
            throw new IllegalStateException("Request feed factory must be configured");
            }

        final RequestFeedFactory factory = feedFactory;
        feedFactory = new RequestFeedFactory()
            {
            public RequestFeed create(int threadNumber, int threadCount)
                {
                return new DurationLimiter(factory.create(threadNumber, threadCount), duration);
                }
            };

        MetricsSnapshot snapshot = runTest();
        feedFactory = factory;

        return snapshot;
        }

    protected MetricsSnapshot runTest()
        {
        metrics = new MetricRegistry();

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

        ScheduledExecutorService scheduler = null;
        ScheduledFuture<?> publisherDaemon = null;
        if (publishInterval > 0)
            {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            publisherDaemon = scheduler.scheduleAtFixedRate(
                    new MetricsPublisher(), publishInterval, publishInterval, TimeUnit.MILLISECONDS);
            }

        try
            {
            latch.await();
            }
        catch (InterruptedException ignore)
            {
            }

        executor.shutdown();
        if (publisherDaemon != null)
            {
            publisherDaemon.cancel(false);
            }
        if (scheduler != null)
            {
            scheduler.shutdownNow();
            }

        return new MetricsSnapshot(name, metrics);
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
                Timer tSuccess = metrics.timer(request.getName() + ".success");
                Timer tFailure = metrics.timer(request.getName() + ".failure");
                try
                    {
                    long    start    = System.nanoTime();
                    boolean success  = request.execute();
                    long    duration = System.nanoTime() - start;
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
            MetricsSnapshot snapshot = new MetricsSnapshot(name, metrics);
            for (Publisher pub : publishers)
                {
                pub.publish(snapshot);
                }
            }
        }

    private static class DurationConverter
            implements CommandLine.ITypeConverter<Duration>
        {
        public Duration convert(String sDuration) throws Exception
            {
            return sDuration.toUpperCase().startsWith("P")
                   ? Duration.parse(sDuration)
                   : Duration.ofSeconds(Long.parseLong(sDuration));
            }
        }
    }
