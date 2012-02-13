package net.femtoparsec.jwhois;

import net.femtoparsec.jwhois.impl.JWhoIs;
import net.femtoparsec.jwhois.utils.Collections;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
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
 * User: Bastien Aracil
 * Date: 24/10/11
 */
public class JWhoIsTest {


    private static void dumpAsText(byte[] bytes) {
        LineIterator iterator = IOUtils.lineIterator(new InputStreamReader(new ByteArrayInputStream(bytes)));
        while (iterator.hasNext()) {
            System.out.println(iterator.nextLine());
        }
    }

    public static void main(String[] args) {
        //Let's create a default WhoIs client
        //the client can be cached avoiding multiple search for the same query
        //just wrap the WhoIs in a CachedWhoIs like new CachedWhoIs(otherWhoIs)
        WhoIs whoIs = new JWhoIs();

        //let's query
        Set<SourceResult> sourceResults = whoIs.request("google", Collections.asSet(Source.ARIN, Source.RIPE));

        //met's dump the results
        for (SourceResult sourceResult : sourceResults) {
            System.out.println("Source : " + sourceResult.getSource());
            System.out.println("Format : " + sourceResult.getFormat());
            System.out.println("valid  : " + sourceResult.isValid());
            if (sourceResult.isValid()) {
                dumpAsText(sourceResult.getData());
            }
            else {
                System.err.println(sourceResult.getErrorMessage());
            }
        }
    }
}
