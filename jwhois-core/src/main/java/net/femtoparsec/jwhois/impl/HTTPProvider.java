package net.femtoparsec.jwhois.impl;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

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
 * Date: 22/10/11
 */
public abstract class HTTPProvider extends AbstractProvider {

    public HTTPProvider(String name, Format resultFormat, Source... providedSources) {
        super(name, resultFormat, true, providedSources);
    }

    @Override
    protected InputStream doRequest(String query, Source source) throws IOException {
        URL url = this.getUrl(query, source);
        return this.doRequest(url.openConnection());
    }

    @Override
    protected InputStream doRequest(String query, Source source, Proxy proxy) throws IOException {
        URL url = this.getUrl(query, source);
        return this.doRequest(url.openConnection(proxy));
    }


    private InputStream doRequest(URLConnection connection) throws IOException {
        this.setUpConnection(connection);
        connection.connect();
        return connection.getInputStream();
    }

    protected void setUpConnection(URLConnection connection) {
        connection.setDoOutput(false);
        connection.setDoInput(true);
    }

    protected abstract URL getUrl(String query, Source source) throws UnsupportedEncodingException, MalformedURLException;
}
