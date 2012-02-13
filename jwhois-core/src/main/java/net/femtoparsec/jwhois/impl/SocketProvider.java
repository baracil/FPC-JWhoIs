package net.femtoparsec.jwhois.impl;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Proxy;
import java.net.Socket;

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
 * A provider that uses socket to get the query result.
 *
 * User: Bastien Aracil
 * Date: 22/10/11
 */
public abstract class SocketProvider extends AbstractProvider {

    /**
     * Create a socket provider
     * @param name the name of the provider
     * @param resultFormat the format of the WhoIs results return by this provider
     * @param providedSources the list of sources this provider can provide
     */
    public SocketProvider(String name, Format resultFormat, Source... providedSources) {
        //Socket provider cannot handle proxy connection, thus the false value for the third parameter
        super(name, resultFormat, false, providedSources);
    }

    @Override
    protected InputStream doRequest(String query, Source source) throws IOException {
        Socket socket = this.getSocket(source);
        PrintStream ps = new PrintStream(socket.getOutputStream());
        ps.println(query);
        return socket.getInputStream();
    }

    /**
     * @param source the source
     * @return a Socket for the given source
     * @throws IOException if an I/O error occurred
     */
    protected abstract Socket getSocket(Source source) throws IOException;

    @Override
    protected InputStream doRequest(String query, Source source, Proxy proxy) throws IOException {
        return null;
    }
}
