package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class LockRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;
    private long wait;

    public LockRequest(NamedCache cache, Object key)
        {
        this(cache, key, 0);
        }

    public LockRequest(NamedCache cache, Object key, long wait)
        {
        this.cache = cache;
        this.key = key;
        this.wait = wait;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.lock(key, wait);
        }

    @Override
    public String getName()
        {
        return "lock." + cache.getCacheName();
        }
    }
