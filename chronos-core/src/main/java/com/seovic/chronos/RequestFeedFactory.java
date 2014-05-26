package com.seovic.chronos;

import java.io.Serializable;

/**
 * Creates a {@link RequestFeed} instances for worker threads.
 *
 * @author Aleksandar Seovic
 */
public interface RequestFeedFactory extends Serializable
{
    RequestFeed create(int threadNumber, int threadCount);
}
