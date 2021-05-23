package com.seovic.chronos.coherence;


import com.seovic.chronos.request.AbstractRequest;

import com.tangosol.net.NamedCache;


/**
 * @author Aleksandar Seovic  2014.05.26
 */
public class GetRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;

    public GetRequest(NamedCache cache, Object key)
        {
        this.cache = cache;
        this.key = key;
        }

    protected Object doExecute() throws Throwable
        {
        return cache.get(key);
        }

    public String getName()
        {
        return "get." + cache.getCacheName();
        }
    }
