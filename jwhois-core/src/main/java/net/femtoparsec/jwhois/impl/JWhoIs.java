package net.femtoparsec.jwhois.impl;

import net.femtoparsec.jwhois.*;
import net.femtoparsec.jwhois.impl.provider.DefaultProviderManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

import java.io.InputStream;
import java.net.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
 * The core implementation of the WhoIs client
 *
 * User: Bastien Aracil
 * Date: 22/10/11
 */
public class JWhoIs implements WhoIs {

    public static Format DEFAULT_PREFERRED_FORMAT = Format.RPSL;

    private final ProviderManager providerManager;

    private Proxy proxy;

    private Format preferredFormat;

    /**
     * Create a JWhoIs client
     */
    public JWhoIs() {
        this.providerManager = new DefaultProviderManager();
        this.preferredFormat = DEFAULT_PREFERRED_FORMAT;
    }

    @Override
    public void setPreferredFormat(Format format) {
        Validate.notNull(format, "PreferredFormat");
        this.preferredFormat = format;
    }

    @Override
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public Set<SourceResult> request(String query, Set<Source> sources) {
        final Map<Source, Provider> providers = providerManager.getProviders(sources, this.proxy != null, this.preferredFormat);
        final RequestEvaluator evaluator = new RequestEvaluator(query, this.proxy, providers);
        final Set<SourceResult> requestResults = new HashSet<SourceResult>(sources.size());

        for (Source source : sources) {
            requestResults.add(evaluator.evaluateRequest(source));
        }

        return requestResults;
    }

    /**
     * inner utility class. Handles source availability, proxy setting ...
     */
    private static class RequestEvaluator {

        private final String query;

        private final Proxy proxy;

        private final Map<Source, Provider> providers;

        private RequestEvaluator(String query, Proxy proxy, Map<Source, Provider> providers) {
            this.query = query;
            this.proxy = proxy;
            this.providers = providers;
        }

        final public SourceResult evaluateRequest(Source source) {
            final boolean withProxy = this.proxy != null;
            Provider provider = providers.get(source);
            SourceResult result;

            try {
                if (provider == null) {
                    result = DefaultSourceResult.createInvalid(this.query, source, String.format("no provider available (with proxy: %s",withProxy));
                }
                else {
                    InputStream providerInputStream;
                    if (withProxy) {
                        providerInputStream = provider.request(this.query, source, this.proxy);
                    }
                    else {
                        providerInputStream = provider.request(this.query, source);
                    }

                    final byte[] data = IOUtils.toByteArray(providerInputStream);

                    result = DefaultSourceResult.createValid(query,source,provider.getResultFormat(), data);
                }
            } catch (Throwable t) {
                result = DefaultSourceResult.createInvalid(this.query, source, t.getMessage());
            }
            return result;
        }
    }
}
