/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.chronos.feed;


import com.seovic.chronos.Request;
import com.seovic.chronos.RequestFeed;


/**
 * A {@link RequestFeed} implementation that always returns the same instance.
 *
 * @author Aleksandar Seovic  2013.04.16
 */
public class SingletonFeed
        implements RequestFeed
    {
    private final Request request;

    /**
     * Create SingletonFeed instance.
     *
     * @param requestClass the class of the request to return (must have default
     *                     constructor)
     */
    public SingletonFeed(Class<? extends Request> requestClass)
        {
        try
            {
            this.request = requestClass.getDeclaredConstructor().newInstance();
            }
        catch (Exception e)
            {
            throw new RuntimeException(e);
            }
        }

    /**
     * Create SingletonFeed instance.
     *
     * @param request request to return
     */
    public SingletonFeed(Request request)
        {
        this.request = request;
        }

    /**
     * {@inheritDoc}
     */
    @Override
    public Request next()
        {
        return request;
        }
    }
