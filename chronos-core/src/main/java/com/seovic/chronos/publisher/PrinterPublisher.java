package com.seovic.chronos.publisher;


import com.seovic.chronos.Publisher;
import com.seovic.chronos.MetricsSnapshot;
import com.seovic.chronos.TimerSnapshot;

import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Date;
import java.util.Map;


/**
 * Prints test results.
 *
 * @author Aleksandar Seovic
 */
public class PrinterPublisher
        implements Publisher
    {
    private final PrintStream out;

    public PrinterPublisher()
        {
        this(System.out);
        }

    public PrinterPublisher(OutputStream out)
        {
        this(new PrintStream(out));
        }

    public PrinterPublisher(PrintStream out)
        {
        this.out = out;
        }

    @Override
    public void publish(MetricsSnapshot metrics)
        {
        Date time = new Date(metrics.getTimestamp());

        for (Map.Entry<String, TimerSnapshot> result : metrics.getTimers().entrySet())
            {
            out.println(time + " - " + result.getKey() + ": " + result.getValue());
            }

        out.flush();
        }
    }
