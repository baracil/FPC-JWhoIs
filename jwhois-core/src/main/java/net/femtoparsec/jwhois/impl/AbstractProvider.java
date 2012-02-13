package net.femtoparsec.jwhois.impl;

import net.femtoparsec.jwhois.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Collections;
import java.util.HashSet;
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
 * A partial implementation of the interface Provider
 *
 * User: Bastien Aracil
 * Date: 22/10/11
 */
public abstract class AbstractProvider implements Provider {

    /**
     * The set of sources the provider can query
     */
    private final Set<Source> providedSources;

    /**
     * Indicates if this provider can handle a proxy connection
     */
    private final boolean handleProxy;

    /**
     * The result format of this provider
     */
    private final Format resultFormat;

    /**
     * The name of the provider
     */
    public final String name;

    /**
     * Create a provider
     * @param name the name of the provider
     * @param resultFormat the format of the WhoIs results return by this provider
     * @param handleProxy true if the provider can handle proxy
     * @param providedSources the list of sources this provider can provide
     */
    public AbstractProvider(String name, Format resultFormat, boolean handleProxy, Source... providedSources) {
        Validate.noNullElements(providedSources, "providedSources");

        Set<Source> set = new HashSet<Source>();
        Collections.addAll(set, providedSources);

        this.handleProxy = handleProxy;
        this.providedSources = set;
        this.resultFormat = resultFormat;
        this.name = name;
    }

    @Override
    final public String getName() {
        return name;
    }

    @Override
    final public Format getResultFormat() {
        return this.resultFormat;
    }

    @Override
    final public Set<Source> getProvidedSources() {
        return new HashSet<Source>(providedSources);
    }

    @Override
    final public boolean handleProxy() {
        return this.handleProxy;
    }

    @Override
    final public boolean canProvide(Source source) {
        return this.getProvidedSources().contains(source);
    }

    @Override
    final public InputStream request(String query, Source source) throws InvalidSourceException, InvalidQueryException, IOException {
        this.checkQuery(query);
        this.checkSource(source);
        return this.doRequest(query, source);
    }

    @Override
    final public InputStream request(String query, Source source, Proxy proxy) throws InvalidSourceException, InvalidQueryException, InvalidProviderException, IOException {
        this.checkQuery(query);
        this.checkSource(source);
        if (!this.handleProxy) {
            throw new InvalidProviderException("provider cannot handle proxy", this);
        }
        return this.doRequest(query, source, proxy);
    }

    /**
     * Same as the {@link Provider#request(String, net.femtoparsec.jwhois.Source)} but after integrity check, i.e.
     * there is no need to check if the query or the source if empty and if this provider can handle the given source
     * @param query the WhoIs query
     * @param source the WhoIs data source queried
     * @throws IOException if an I/O exception occurred during the request
     * @return an inputStream from which the query result can be read
     */
    protected abstract InputStream doRequest(String query, Source source) throws IOException;

    /**
     * Same as the {@link Provider#request(String, net.femtoparsec.jwhois.Source, Proxy)} but after integrity check, i.e.
     * there is no need to check if the query or the source if empty and if this provider can handle proxy and the given source
     * @param query the WhoIs query
     * @param source the WhoIs data source queried
     * @param proxy the proxy information
     * @return an inputStream from which the query result can be read
     * @throws IOException if an I/O exception occurred during the request
     */
    protected abstract InputStream doRequest(String query, Source source, Proxy proxy) throws IOException;

    private void checkQuery(String query) throws InvalidQueryException {
        if (StringUtils.isEmpty(query)) {
            throw new InvalidQueryException(query, "Query is empty");
        }
    }

    private void checkSource(Source source) throws InvalidSourceException {
        if (!this.canProvide(source)) {
            throw new InvalidSourceException(this, source);
        }
    }

}
