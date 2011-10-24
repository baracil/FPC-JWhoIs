package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.Source;
import net.femtoparsec.jwhois.SourceProvider;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
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
 * Date: 23/10/11
 */
public class JResultsGUI<S extends SourceProvider> extends JTabbedPane implements SourceSelectionListener {

    private final Map<String, Integer> sourceNameIdx = new HashMap<String, Integer>();

    private final JWhoIsGUIModel<S> model;

    private final Map<Source, JResultGUI<S>> jResultGUIMap = new HashMap<Source, JResultGUI<S>>();

    public JResultsGUI(JWhoIsGUIModel<S> model) {
        this.model = model;

        Set<Source> proposedSources = model.getSources();
        Set<Source> selectedSources = model.getSelectedSources();

        int idx = 0;
        for (Source source : Source.getOrderedSources()) {
            if (proposedSources.contains(source) && selectedSources.contains(source)) {
                JResultGUI<S> resultGUI = this.createJResultGUI(source);
                this.addTab(source.name(), resultGUI);
            }

            sourceNameIdx.put(source.name(), idx++);
        }

        this.model.addSourceSelectionListener(this);
    }

    private JResultGUI<S> createJResultGUI(Source source) {
        JResultGUI<S> resultGUI = model.createJResultGUI(source);
        this.jResultGUIMap.put(source, resultGUI);
        return resultGUI;
    }

    @Override
    public void onSourceSelected(Source source, boolean selected) {
        if (selected) {
            String title = source.name();
            int idx = findInsertionIndex(source);
            JResultGUI<S> gui = this.createJResultGUI(source);
            this.insertTab(title, null, gui, null, idx);
        }
        else {
            JResultGUI<S> gui = this.jResultGUIMap.remove(source);
            int idx = this.findTabIndex(source);
            if (idx >= 0) {
                this.removeTabAt(idx);
            }
            if (gui != null) {
                gui.onRemove();
            }
        }
    }

    private int findTabIndex(Source source) {
        for (int idx = 0; idx < this.getTabCount(); idx++) {
            if (this.getTitleAt(idx).equals(source.name())) {
                return idx;
            }
        }

        return -1;
    }

    private int findInsertionIndex(Source source) {
        int idx;
        int nbTabs = this.getTabCount();
        int[] titleIndexes = new int[nbTabs];
        for (idx = 0; idx < nbTabs; idx++) {
            titleIndexes[idx] = sourceNameIdx.get(this.getTitleAt(idx));
        }

        int searchIndex = sourceNameIdx.get(source.name());

        for (idx = 0; idx < nbTabs; idx++) {
            if (titleIndexes[idx] > searchIndex) {
                return idx;
            }
        }

        return nbTabs;

    }
}
