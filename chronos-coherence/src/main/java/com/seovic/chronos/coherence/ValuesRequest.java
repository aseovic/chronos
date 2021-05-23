package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class ValuesRequest extends AbstractRequest
    {
    private NamedCache cache;

    public ValuesRequest(NamedCache cache)
        {
        this.cache = cache;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.values();
        }

    @Override
    public String getName()
        {
        return "values." + cache.getCacheName();
        }
    }
