package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class UnlockRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;

    public UnlockRequest(NamedCache cache, Object key)
        {
        this.cache = cache;
        this.key = key;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.unlock(key);
        }

    @Override
    public String getName()
        {
        return "unlock." + cache.getCacheName();
        }
    }
