package net.femtoparsec.jwhois;

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
 *
 */

/**
 * A WhoIs client that returns {@link SourceResult} data.
 * 
 * User: Bastien Aracil
 * Date: 21/10/11
 */
public interface WhoIs extends GenericWhoIs<SourceResult> {

    /**
     * @param format the preferred format. If multiple providers are available
     * for a given {@link Source}, the provider that returns data in the preferred format
     * will be selected in priority.
     */
    void setPreferredFormat(Format format);

    /**
     * @param proxy the proxy needed to connect to the providers. If no proxy is needed, the value null
     * must be used.
     */
    void setProxy(Proxy proxy);

    /**
     * @param query the WhoIs query
     * @param sources the {@link Source}
     * @return the results of the query performed on the given sources
     */
    Set<SourceResult> request(String query, Set<Source> sources);

}
