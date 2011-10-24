package net.femtoparsec.jwhois.test.text;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Source;
import net.femtoparsec.jwhois.text.TextSourceResult;
import net.femtoparsec.jwhois.text.TextWhoIs;
import net.femtoparsec.jwhois.utils.Collections;

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
 * Date: 22/10/11
 */
public class TextWhoIsTest {

    private static void print(String[] lines) {
        for (String line : lines) {
            System.err.println(line);
        }
    }

    public static void main(String[] args) {
        //This is basically the same thing than in JWhoIsTest but the conversion from byte[] to String[]
        //is made by the client here.

        TextWhoIs whoIs = new TextWhoIs();
        whoIs.setPreferredFormat(Format.RPSL);

        Set<TextSourceResult> results = whoIs.request("yahoo.com", Collections.asSet(Source.RIPE, Source.ARIN));

        for (TextSourceResult result : results) {
            System.err.println("Source : " + result.getSource());
            System.err.println(" valid : "+result.isValid());
            if (result.isValid()) {
                print(result.getText());
            }
            else {
                System.err.println(result.getErrorMessage());
            }
        }

    }

}
