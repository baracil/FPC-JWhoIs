package net.femtoparsec.jwhois;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
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
 * A provider is responsible to answer a WhoIs query for a given source.
 *
 * User: Bastien Aracil
 * Date: 22/10/11
 */
public interface Provider {

    /**
     * @return the name of the provider
     */
    String getName();

    /**
     * @return the sources this provider can povide
     */
    Set<Source> getProvidedSources();

    /**
     * @param source the source to test
     * @return true if this provider can provide WhoIs information for the given source.
     *
     * This is equivalent to a test of existence of the given source in the set returns by the method {#getProvidedSources}.
     */
    boolean canProvide(Source source);

    /**
     * @return true if this provider can handle a proxy connection, false otherwise
     */
    boolean handleProxy();

    /**
     * Perform the WhoIs query for a given source. The request is done without proxy.
     *
     * @param query the WhoIs query
     * @param source the WhoIs data source queried
     * @return an inputStream from which the query result can be read
     * @throws InvalidSourceException If the provider cannot provide query information for the given source
     * @throws InvalidQueryException if the query is invalid
     * @throws IOException if an I/O exception occurred during the request
     */
    InputStream request(String query, Source source) throws InvalidSourceException, InvalidQueryException, IOException;

    /**
     * Perform the WhoIs query for a given source. The request is done with proxy.
     *
     * @param query the WhoIs query
     * @param source the WhoIs data source queried
     * @param proxy the proxy information
     * @return an inputStream from which the query result can be read
     * @throws InvalidSourceException if the provider cannot provide query information for the given source
     * @throws InvalidQueryException if the query is invalid
     * @throws InvalidProviderException if the provider cannot handle a proxy connection
     * @throws IOException if an I/O exception occurred during the request
     */
    InputStream request(String query, Source source, Proxy proxy) throws InvalidSourceException, InvalidQueryException, InvalidProviderException, IOException;

    Format getResultFormat();
}
