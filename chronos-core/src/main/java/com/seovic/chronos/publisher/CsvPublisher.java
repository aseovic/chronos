package com.seovic.chronos.publisher;


import com.seovic.chronos.MetricsSnapshot;
import com.seovic.chronos.Publisher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Publishes test results to a CSV file.
 *
 * @author Aleksandar Seovic
 */
public class CsvPublisher
        implements Publisher
    {
    private OutputStream out;

    public CsvPublisher(String fileName) throws FileNotFoundException
        {
        this(new File(fileName));
        }

    public CsvPublisher(File file) throws FileNotFoundException
        {
        this(new FileOutputStream(file));
        }

    public CsvPublisher(OutputStream out)
        {
        this.out = out;
        }

    public void publish(MetricsSnapshot metrics)
        {
        }
    }
