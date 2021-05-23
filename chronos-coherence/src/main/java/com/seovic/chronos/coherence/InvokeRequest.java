package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;

/**
 * @author Vaso Putica  2014.05.26
 */
public class InvokeRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Object key;
    private InvocableMap.EntryProcessor agent;

    public InvokeRequest(NamedCache cache, Object key, InvocableMap.EntryProcessor agent)
        {
        this.cache = cache;
        this.key = key;
        this.agent = agent;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return cache.invoke(key, agent);
        }

    @Override
    public String getName()
        {
        return "invoke." + cache.getCacheName();
        }
    }
