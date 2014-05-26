package com.seovic.chronos;

/**
 * @author Aleksandar Seovic
 */
public interface Request
{
    /**
     * Request name.
     *
     * @return request name
     */
    String getName();

    /**
     * Execute request.
     *
     * @return true if successful, false if failed
     */
    boolean execute();
}
