package com.seovic.chronos.publisher;

import com.seovic.chronos.Publisher;
import com.seovic.chronos.MetricsSnapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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

    public PrinterPublisher(File file)
        {
        this(Publisher.createFileOutputStream(file));
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
        out.println(metrics);
        out.flush();
        }
    }
