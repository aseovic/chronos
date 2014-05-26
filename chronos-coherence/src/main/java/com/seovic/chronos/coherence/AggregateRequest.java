package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;

import java.util.Collection;

/**
 * @author Vaso Putica  2014.05.26
 */
public class AggregateRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Filter filter;
    private String filterName;
    private Collection collKeys;
    private InvocableMap.EntryAggregator agent;

    public AggregateRequest(NamedCache cache, Filter filter, String filterName, InvocableMap.EntryAggregator agent)
        {
        this(agent, cache);
        this.filter = filter;
        this.filterName = filterName;
        }

    public AggregateRequest(NamedCache cache, Collection collKeys, InvocableMap.EntryAggregator agent)
        {
        this(agent, cache);
        this.collKeys = collKeys;
        }

    private AggregateRequest(InvocableMap.EntryAggregator agent, NamedCache cache)
        {
        this.agent = agent;
        this.cache = cache;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        return filter == null
                ? cache.aggregate(collKeys, agent)
                : cache.aggregate(filter, agent);
        }

    @Override
    public String getName()
        {
        StringBuilder name = new StringBuilder("aggregate.")
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
