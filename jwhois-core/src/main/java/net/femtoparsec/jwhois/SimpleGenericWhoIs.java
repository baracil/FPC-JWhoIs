package net.femtoparsec.jwhois;

import net.femtoparsec.jwhois.impl.JWhoIs;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang.Validate;

import java.net.Proxy;
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
 * A basic implementation of the {@link GenericWhoIs} client. This implementation use the core {@link WhoIs} client
 * and a transformer to get the result in the generic type.
 * 
 * User: Bastien Aracil
 * Date: 23/10/11
 */
public class SimpleGenericWhoIs<S> implements GenericWhoIs<S> {

    private final WhoIs whoIs;

    private final Transformer<SourceResult, S> transformer;

    /**
     * Create a SimpleGenericWhoIs with the default {@link WhoIs} client implementation : {@link JWhoIs}
     * @param transformer The transformer used to convert the byte[] data into the generic object
     */
    public SimpleGenericWhoIs(Transformer<SourceResult, S> transformer) {
        this(new JWhoIs(), transformer);
    }

    /**
     * Create a SimpleGenericWhoIs
     * @param whoIs the basic WhoIs client used to performed the queries
     * @param transformer the transformer used to convert the result of the given WhoIs client to the generic format
     */
    public SimpleGenericWhoIs(WhoIs whoIs, Transformer<SourceResult, S> transformer) {
        Validate.notNull(whoIs, "whoIs");
        Validate.notNull(transformer, "transformer");
        this.whoIs = whoIs;
        this.transformer = transformer;
    }

    @Override
    public void setPreferredFormat(Format format) {
        this.whoIs.setPreferredFormat(format);
    }

    @Override
    public void setProxy(Proxy proxy) {
        this.whoIs.setProxy(proxy);
    }

    @Override
    public Set<S> request(String query, Set<Source> sources) {
        Set<SourceResult> sourceResults = this.whoIs.request(query, sources);

        Set<S> results = new HashSet<S>(sourceResults.size());
        for (SourceResult sourceResult : sourceResults) {
            results.add(transformer.transform(sourceResult));
        }

        return results;
    }
}
