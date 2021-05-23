package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class SizeRequest extends AbstractRequest
    {
    private NamedCache cache;

    public SizeRequest(NamedCache cache)
        {
        this.cache = cache;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.size();
        }

    @Override
    public String getName()
        {
        return "size." + cache.getCacheName();
        }
    }
