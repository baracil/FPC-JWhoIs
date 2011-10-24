package net.femtoparsec.jwhois.impl;

import net.femtoparsec.jwhois.Source;
import net.femtoparsec.jwhois.SourceResult;
import net.femtoparsec.jwhois.WhoIs;

import java.util.*;

/*
 *
 *     Copyright (c) 2011, Bastien Aracil
 *     All rights reserved.
 *     New BSD license. See http://en.wikipedia.org/wiki/Bsd_license
 *
 *     Redistribution and use in source and binary forms, with or without
 *     modification, are permitted provided that the following conditions are met:
 *        * Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *        * Redistributions in binary form must reproduce the above copyright
 *          notice, this list of conditions and the following disclaimer in the
 *          documentation and/or other materials provided with the distribution.
 *        * The name of Bastien Aracil may not be used to endorse or promote products
 *          derived from this software without specific prior written permission.
 *
 *     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *     ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *     WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *     DISCLAIMED. IN NO EVENT SHALL BASTIEN ARACIL BE LIABLE FOR ANY
 *     DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *     (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *     LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *     ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *     (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *     SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * /
 */

/**
 * User: Bastien Aracil
 * Date: 21/10/11
 */
public class CachedWhoIs extends ProxyWhoIs {

    private final Map<String, Map<Source, SourceResult>> cache = new TreeMap<String, Map<Source, SourceResult>>();

    public CachedWhoIs(WhoIs delegate) {
        super(delegate);
    }

    @Override
    public Set<SourceResult> request(String query, Set<Source> sources) {
        Set<Source> missingSource = new HashSet<Source>();

        Set<SourceResult> results = new HashSet<SourceResult>(sources.size());

        for (Source source : sources) {
            SourceResult result = this.getFromCache(query, source);
            if (result == null) {
                missingSource.add(source);
            }
            else {
                results.add(result);
            }
        }

        if (!missingSource.isEmpty()) {
            Set<SourceResult> missingResults = super.request(query, missingSource);
            for (SourceResult result : missingResults) {
                this.setToCache(query, result);
                results.add(result);
            }
        }

        return results;
    }

    private SourceResult getFromCache(String query, Source source) {
        final String queryKey = query.toLowerCase();

        Map<Source, SourceResult> map = this.cache.get(queryKey);
        return map == null ? null : map.get(source);
    }

    private void setToCache(String query, SourceResult result) {
        final String queryKey = query.toLowerCase();

        Map<Source, SourceResult> map = this.cache.get(queryKey);
        if (map == null) {
            map = new IdentityHashMap<Source, SourceResult>();
            this.cache.put(queryKey, map);
        }
        map.put(result.getSource(), result);
    }

    public void clearCache() {
        this.cache.clear();
    }
}
