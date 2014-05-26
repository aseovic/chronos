package com.seovic.chronos.coherence;

import com.seovic.chronos.request.AbstractRequest;
import com.tangosol.net.NamedCache;

import java.util.Map;

/**
 * @author Vaso Putica  2014.05.26
 */
public class PutAllRequest extends AbstractRequest
    {
    private NamedCache cache;
    private Map map;

    public PutAllRequest(NamedCache cache, Map map)
        {
        this.cache = cache;
        this.map = map;
        }

    @Override
    protected Object doExecute() throws Exception
        {
        cache.putAll(map);
        return null;
        }

    @Override
    public String getName()
        {
        return "putAll." + cache.getCacheName();
        }
    }
