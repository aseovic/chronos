package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class ClearRequest extends AbstractRequest
    {
    private NamedCache cache;

    public ClearRequest(NamedCache cache)
        {
        this.cache = cache;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        cache.clear();
        return null;
        }

    @Override
    public String getName()
        {
        return "clear." + cache.getCacheName();
        }
    }
