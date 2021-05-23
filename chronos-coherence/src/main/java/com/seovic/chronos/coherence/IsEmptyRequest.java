package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class IsEmptyRequest extends AbstractRequest
    {
    private NamedCache cache;

    public IsEmptyRequest(NamedCache cache)
        {
        this.cache = cache;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.isEmpty();
        }

    @Override
    public String getName()
        {
        return "isEmpty." + cache.getCacheName();
        }
    }
