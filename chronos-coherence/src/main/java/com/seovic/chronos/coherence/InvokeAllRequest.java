package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;

import java.util.Collection;

/**
 * @author Vaso Putica  2014.05.26
 */
public class InvokeAllRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Collection collKeys;
    private Filter filter;
    private String filterName;
    private InvocableMap.EntryProcessor agent;

    public InvokeAllRequest(NamedCache cache, Collection collKeys, InvocableMap.EntryProcessor agent)
        {
        this(cache, agent);
        this.collKeys = collKeys;
        }

    public InvokeAllRequest(NamedCache cache, Filter filter, String filterName, InvocableMap.EntryProcessor agent)
        {
        this(cache, agent);
        this.filter = filter;
        this.filterName = filterName;
        }

    private InvokeAllRequest(NamedCache cache, InvocableMap.EntryProcessor agent)
        {
        this.cache = cache;
        this.agent = agent;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        return filter == null
                ? cache.invokeAll(collKeys, agent)
                : cache.invokeAll(filter, agent);
        }

    @Override
    public String getName()
        {
        StringBuilder name = new StringBuilder("invokeAll.")
                .append(cache.getCacheName())
                .append('.')
                .append(agent.getClass().getSimpleName());
        if (filter != null)
            {
            name.append('.').append(filterName);
            }
        return name.toString();
        }
    }
