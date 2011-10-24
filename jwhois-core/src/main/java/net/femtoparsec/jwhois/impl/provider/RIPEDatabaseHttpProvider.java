package net.femtoparsec.jwhois.impl.provider;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Source;
import net.femtoparsec.jwhois.impl.HTTPProvider;
import org.apache.commons.lang.Validate;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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
public class RIPEDatabaseHttpProvider extends HTTPProvider {

    private static final String RIPE_TEMPLATE_REST_URL = "http://lab.db.ripe.net/whois/search?source=%s&flags=rC&query-string=%s";

    public RIPEDatabaseHttpProvider(Format format) {
        super(String.format("RIPE Database from REST Service (%s)",format), format, Source.RIPE, Source.APNIC, Source.AfriNIC);
        Validate.isTrue(format == Format.XML || format == Format.JSON, "Invalid format. Only XML and JSON are accepted");
    }

    @Override
    protected void setUpConnection(URLConnection connection) {
        super.setUpConnection(connection);

        String acceptValue;
        switch (this.getResultFormat()) {
            case JSON: acceptValue = "application/json";break;
            case XML: acceptValue = "text/xml";break;
            default:acceptValue = null;
        }

        if (acceptValue != null) {
            connection.addRequestProperty("Accept", acceptValue);
        }
    }

    @Override
    protected URL getUrl(String query, Source source) throws MalformedURLException {
        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(query,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedQuery = "";
        }

        return new URL(String.format(RIPE_TEMPLATE_REST_URL, getSourceArgument(source), encodedQuery));
    }

    private String getSourceArgument(Source source) {
        switch (source) {
            case RIPE: return "ripe";
            case APNIC: return "apnic";
            case AfriNIC: return "afrinic";
            default: return null;
        }
    }
}
