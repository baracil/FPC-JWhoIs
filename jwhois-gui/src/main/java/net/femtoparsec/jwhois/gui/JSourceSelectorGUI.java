package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.Source;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class JSourceSelectorGUI extends JPanel implements ActionListener {

    private final JWhoIsGUIModel<?> model;

    private final Map<Source, JCheckBox> checkBoxMap = new HashMap<Source, JCheckBox>();

    public JSourceSelectorGUI(JWhoIsGUIModel<?> model) {
        this.model = model;

        this.setLayout(new FlowLayout());

        Set<Source> proposedSources = model.getSources();
        Set<Source> selectedSources = model.getSelectedSources();

        for (Source source : Source.getOrderedSources()) {
            if (proposedSources.contains(source)) {
                JCheckBox checkBox = new JCheckBox(source.name());
                this.checkBoxMap.put(source, checkBox);
                this.add(checkBox);
                checkBox.setSelected(selectedSources.contains(source));
                checkBox.setActionCommand(source.name());
                checkBox.addActionListener(this);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Source source = Source.valueOf(e.getActionCommand());
        this.model.selectSource(source, checkBoxMap.get(source).isSelected());
    }
}