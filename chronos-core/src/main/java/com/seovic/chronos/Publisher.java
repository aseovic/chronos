package com.seovic.chronos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Publishes test statistics.
 *
 * @author Aleksandar Seovic
 */
public interface Publisher
    {
    void publish(MetricsSnapshot metrics);

    static FileOutputStream createFileOutputStream(File out)
        {
        try
            {
            return new FileOutputStream(out);
            }
        catch (FileNotFoundException e)
            {
            throw new RuntimeException(e);
            }
        }

    static String getLocalHostName()
        {
        try
            {
            return InetAddress.getLocalHost().toString();
            }
        catch (UnknownHostException e)
            {
            return "localhost";
            }
        }
    }
