package net.femtoparsec.jwhois.text;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.Source;
import net.femtoparsec.jwhois.SourceProvider;
import net.femtoparsec.jwhois.SourceResult;
import net.femtoparsec.jwhois.utils.ByteUtils;
import org.apache.commons.lang.StringUtils;

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
 * Date: 23/10/11
 */
public class TextSourceResult implements SourceProvider {

    private final String query;

    private final Source source;

    private final Format format;

    private final String[] text;

    private final String errorMessage;

    public TextSourceResult(SourceResult sourceResult) {
        this(sourceResult.getQuery(), sourceResult.getSource(), sourceResult.getFormat(), ByteUtils.bytesToStrings(sourceResult.getData()), sourceResult.getErrorMessage());
    }

    public TextSourceResult(String query, Source source, Format format, String[] text, String errorMessage) {
        this.query = query;
        this.source = source;
        this.format = format;
        this.text = text;
        this.errorMessage = errorMessage;
    }

    public String getQuery() {
        return query;
    }

    public Source getSource() {
        return source;
    }

    public Format getFormat() {
        return format;
    }

    public String[] getText() {
        return text;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isValid() {
        return StringUtils.isEmpty(this.errorMessage);
    }
}
