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

import java.util.*;

/**
 * A {@link com.seovic.chronos.RequestFeed} implementation that iterates over collection of requests.
 * <p/>
 * When the collection is exhausted this feed will restart iteration from the beginning of the collection,
 * and optionally shuffle collection elements.
 *
 * @author Aleksandar Seovic  2013.04.16
 */
public class CollectionFeed
        implements RequestFeed {
    private boolean shuffle;
    private List<? extends Request> requests;
    private Iterator<? extends Request> iterator;

    /**
     * Create CollectionFeed instance.
     *
     * @param requests  requests to iterate over
     */
    public CollectionFeed(Request... requests) {
        this(false, Arrays.asList(requests));
    }

    /**
     * Create CollectionFeed instance.
     *
     * @param shuffle   whether to shuffle requests in the collection before each iteration
     * @param requests  requests to iterate over
     */
    public CollectionFeed(boolean shuffle, Request... requests) {
        this(shuffle, Arrays.asList(requests));
    }

    /**
     * Create CollectionFeed instance.
     *
     * @param requests  requests to iterate over
     */
    public CollectionFeed(Collection<? extends Request> requests) {
        this(false, requests);
    }

    /**
     * Create CollectionFeed instance.
     *
     * @param shuffle   whether to shuffle requests in the collection before each iteration
     * @param requests  requests to iterate over
     */
    public CollectionFeed(boolean shuffle, Collection<? extends Request> requests) {
        if (requests == null || requests.size() == 0) {
            throw new IllegalArgumentException("requests argument cannot be null or empty");
        }
        this.shuffle = shuffle;
        this.requests = new ArrayList<>(requests);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Request next() {
        if (iterator == null || !iterator.hasNext()) {
            if (shuffle) {
                Collections.shuffle(this.requests);
            }
            iterator = requests.iterator();
        }

        return iterator.next();
    }
}
