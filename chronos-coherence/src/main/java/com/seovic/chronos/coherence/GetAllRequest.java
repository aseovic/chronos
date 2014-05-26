package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

import java.util.Collection;

/**
 * @author Vaso Putica  2014.05.26
 */
public class GetAllRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Collection colKeys;

    public GetAllRequest(NamedCache cache, Collection colKeys)
        {
        this.cache = cache;
        this.colKeys = colKeys;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        return cache.getAll(colKeys);
        }

    @Override
    public String getName()
        {
        return "getAll." + cache.getCacheName();
        }
    }
