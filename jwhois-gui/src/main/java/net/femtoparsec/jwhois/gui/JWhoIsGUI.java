package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.GenericWhoIs;
import net.femtoparsec.jwhois.SourceProvider;

import javax.swing.*;
import java.awt.*;

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
public class JWhoIsGUI<S extends SourceProvider> extends JPanel {

    private final JWhoIsGUIModel<S> model;

    private final JWhoIsQueryGUI queryGUI;

    private final JSourceSelectorGUI sourceSelectorGUI;

    private final JResultsGUI<S> resultsGUI;

    private final JFormatSelectorGUI formatSelectorGUI;

    public JWhoIsGUI(JWhoIsGUIModel<S> model) {
        this(model,true);
    }
    public JWhoIsGUI(GenericWhoIs<S> whoIs, JResultGUIFactory<S> factory) {
        this(whoIs, factory, true);
    }

    public JWhoIsGUI(JWhoIsGUIModel<S> model, boolean withFormatSelector) {
        this.model = model;
        this.setLayout(new BorderLayout());

        this.queryGUI = new JWhoIsQueryGUI(this.model);
        this.sourceSelectorGUI = new JSourceSelectorGUI(this.model);
        this.resultsGUI = new JResultsGUI<S>(this.model);

        JPanel top = new JPanel(new GridLayout(2+(withFormatSelector?1:0),1));
        top.add(this.queryGUI);
        if (withFormatSelector) {
            this.formatSelectorGUI = new JFormatSelectorGUI(this.model);
            top.add(this.formatSelectorGUI);
        } else {
            this.formatSelectorGUI = new JFormatSelectorGUI(this.model);

        }
        top.add(this.sourceSelectorGUI);

        this.add(top, BorderLayout.NORTH);
        this.add(this.resultsGUI, BorderLayout.CENTER);
    }

    public JWhoIsGUI(GenericWhoIs<S> whoIs, JResultGUIFactory<S> factory, boolean withFormatSelector) {
        this(new JWhoIsGUIModel<S>(whoIs, factory),withFormatSelector);
    }

    public JWhoIsQueryGUI getQueryGUI() {
        return queryGUI;
    }

    public JSourceSelectorGUI getSourceSelectorGUI() {
        return sourceSelectorGUI;
    }

    public JResultsGUI<S> getResultsGUI() {
        return resultsGUI;
    }
}
