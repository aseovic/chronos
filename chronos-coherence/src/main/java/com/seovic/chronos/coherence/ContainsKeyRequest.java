package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class ContainsKeyRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;

    public ContainsKeyRequest(NamedCache cache, Object key)
        {
        this.cache = cache;
        this.key = key;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        return cache.containsKey(key);
        }

    @Override
    public String getName()
        {
        return "containsKey." + cache.getCacheName();
        }
    }
