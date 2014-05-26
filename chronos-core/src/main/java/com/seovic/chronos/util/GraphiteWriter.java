package com.seovic.chronos.util;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Graphite writer.
 *
 * @author Aleksandar Seovic
 */
public class GraphiteWriter extends PrintWriter {
    public GraphiteWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public void write(String name, double value, long timestamp) {
        write(name);
        write(' ');
        write(Double.toString(value));
        write(' ');
        write(Long.toString(timestamp));
        write('\n');
    }
}
