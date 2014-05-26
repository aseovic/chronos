package com.seovic.chronos.request;


import com.seovic.chronos.Request;


/**
 * @author Aleksandar Seovic  2014.05.26
 */
public abstract class AbstractRequest
        implements Request
    {

    public boolean execute()
        {
        try
            {
            doExecute();
            return true;
            }
        catch (Exception e)
            {
            return false;
            }
        }

    protected abstract Object doExecute() throws Exception;
    }
