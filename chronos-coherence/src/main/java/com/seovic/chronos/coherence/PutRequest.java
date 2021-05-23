package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class PutRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;
    private Object value;

    public PutRequest(NamedCache cache, Object key, Object value)
        {
        this.cache = cache;
        this.key = key;
        this.value = value;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.put(key, value);
        }

    @Override
    public String getName()
        {
        return "put." + cache.getCacheName();
        }
    }
