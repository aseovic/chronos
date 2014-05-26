package com.seovic.chronos;

/**
 * Publishes test statistics.
 *
 * @author Aleksandar Seovic
 */
public interface Publisher {
    void publish(MetricsSnapshot metrics);
}
