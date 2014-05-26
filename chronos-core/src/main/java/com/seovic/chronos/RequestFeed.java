package com.seovic.chronos;

import java.io.Serializable;

/**
 * Provides a feed of {@link Request}s to execute.
 *
 * @author Aleksandar Seovic
 */
public interface RequestFeed extends Serializable
{
    Request next();
}
