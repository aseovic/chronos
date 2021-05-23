package com.seovic.chronos.publisher;


import com.seovic.chronos.Publisher;
import com.seovic.chronos.MetricsSnapshot;
import com.seovic.chronos.TimerSnapshot;

import com.seovic.chronos.util.GraphiteWriter;

import java.net.Socket;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Publishes test results to Graphite.
 *
 * @author Aleksandar Seovic
 */
public class GraphitePublisher
        implements Publisher
    {
    private static final Logger LOG =
            Logger.getLogger(GraphitePublisher.class.getCanonicalName());

    private final String host;
    private final int port;
    private final String localHost;

    public GraphitePublisher(String hostAndPort)
        {
        String[] parts = hostAndPort.split(":");
        if (parts.length != 2)
            {
            throw new IllegalArgumentException("Host and port for the Graphite server must be specified in host:port format");
            }

        this.host = parts[0];
        this.port = Integer.parseInt(parts[1]);
        this.localHost = Publisher.getLocalHostName();
        }

    public GraphitePublisher(String host, int port)
        {
        this.host = host;
        this.port = port;
        this.localHost = Publisher.getLocalHostName();
        }

    @Override
    public void publish(MetricsSnapshot metrics)
        {
        long time = metrics.getTimestamp() / 1000;
        GraphiteWriter writer = null;
        try
            {
            writer = connect();

            for (Map.Entry<String, TimerSnapshot> result : metrics.getTimers().entrySet())
                {
                String prefix = "chronos." + metrics.getTestName() + localHost + "." + result.getKey() + ".";
                TimerSnapshot stats = result.getValue();

                writer.write(prefix + "count", stats.getCount(), time);
                writer.write(prefix + "median", stats.getMedian(), time);
                writer.write(prefix + "75th", stats.get75thPercentile(), time);
                writer.write(prefix + "95th", stats.get95thPercentile(), time);
                writer.write(prefix + "98th", stats.get98thPercentile(), time);
                writer.write(prefix + "99th", stats.get99thPercentile(), time);
                writer.write(prefix + "999th", stats.get999thPercentile(), time);
                writer.write(prefix + "max", stats.getMax(), time);
                writer.write(prefix + "min", stats.getMin(), time);
                writer.write(prefix + "mean", stats.getMean(), time);
                writer.write(prefix + "std-dev", stats.getStdDev(), time);
                writer.write(prefix + "mean-rate", stats.getThroughput(), time);
                }
            writer.flush();
            }
        catch (Throwable e)
            {
            LOG.log(Level.SEVERE,
                    "An error occurred while publishing test statistics to Graphite", e);
            }
        finally
            {
            if (writer != null)
                {
                writer.close();
                }
            }
        }

    private GraphiteWriter connect() throws Exception
        {
        Socket socket = new Socket(host, port);
        return new GraphiteWriter(socket.getOutputStream(), true);
        }
    }
