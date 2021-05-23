package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;

import java.util.Comparator;

/**
 * @author Vaso Putica  2014.05.26
 */
public class EntrySetRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Filter filter;
    private String filterName;
    private Comparator comparator;
    private boolean sorted;

    public EntrySetRequest(NamedCache cache)
        {
        this.cache = cache;
        }

    public EntrySetRequest(NamedCache cache, Filter filter, String filterName)
        {
        this.cache = cache;
        this.filter = filter;
        this.filterName = filterName;
        }

    public EntrySetRequest(NamedCache cache, Filter filter, String filterName, Comparator comparator)
        {
        this(cache, filter, filterName);
        this.comparator = comparator;
        this.sorted = true;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        if (filter == null)
            {
            return cache.entrySet();
            }
        return sorted
                ? cache.entrySet(filter, comparator)
                : cache.entrySet(filter);
        }

    @Override
    public String getName()
        {
        String name = "entrySet." + cache.getCacheName();
        if (filter != null)
            {
            name += "." + filterName;
            }
        return name;
        }
    }
