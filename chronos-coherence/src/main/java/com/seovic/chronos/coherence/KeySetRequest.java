package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;

/**
 * @author Vaso Putica  2014.05.26
 */
public class KeySetRequest extends AbstractRequest
    {
    private String filterName;
    private NamedCache cache;
    private Filter filter;

    public KeySetRequest(NamedCache cache)
        {
        this.cache = cache;
        }

    public KeySetRequest(NamedCache cache, Filter filter, String filterName)
        {
        this.cache = cache;
        this.filter = filter;
        this.filterName = filterName;
        }

    @Override
    protected Object doExecute() throws Throwable
        {
        return filter == null
                ? cache.keySet()
                : cache.keySet(filter);
        }

    @Override
    public String getName()
        {
        String name = "keySet." + cache.getCacheName();
        if (filter != null)
            {
            name += "." + filterName;
            }
        return name;
        }
    }
