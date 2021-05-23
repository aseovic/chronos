package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class RemoveRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;

    public RemoveRequest(NamedCache cache, Object key)
        {
        this.cache = cache;
        this.key = key;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.remove(key);
        }

    @Override
    public String getName()
        {
        return "remove." + cache.getCacheName();
        }
    }
