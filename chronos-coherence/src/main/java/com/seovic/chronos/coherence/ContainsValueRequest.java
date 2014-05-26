package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

/**
 * @author Vaso Putica  2014.05.26
 */
public class ContainsValueRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object value;

    public ContainsValueRequest(NamedCache cache, Object value)
        {
        this.cache = cache;
        this.value = value;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        return cache.containsValue(value);
        }

    @Override
    public String getName()
        {
        return "containsValue." + cache.getCacheName();
        }
    }
