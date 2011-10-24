package net.femtoparsec.jwhois.impl.provider;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Source;
import net.femtoparsec.jwhois.impl.SocketProvider;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
public class DefaultSocketProvider extends SocketProvider {

    private Map<Source, SocketInfo> socketsInfo = new HashMap<Source, SocketInfo>();

    public DefaultSocketProvider() {
        super("Default socket providers", Format.RPSL, Source.ARIN, Source.RIPE, Source.RIPN, Source.APNIC, Source.LACNIC, Source.AfriNIC);

        this.addSocketInfo(new SocketInfo(Source.ARIN,    "whois.arin.net"));
        this.addSocketInfo(new SocketInfo(Source.APNIC,   "whois.apnic.net"));
        this.addSocketInfo(new SocketInfo(Source.RIPE,    "whois.ripe.net"));
        this.addSocketInfo(new SocketInfo(Source.RIPN,    "whois.ripn.net"));
        this.addSocketInfo(new SocketInfo(Source.LACNIC,  "whois.lacnic.net"));
        this.addSocketInfo(new SocketInfo(Source.AfriNIC, "whois.afrinic.net"));
    }

    @Override
    protected Socket getSocket(Source source) throws IOException {
        return this.socketsInfo.get(source).createSocket();
    }

    private void addSocketInfo(SocketInfo socketInfo) {
        this.socketsInfo.put(socketInfo.getSource(), socketInfo);
    }


}
