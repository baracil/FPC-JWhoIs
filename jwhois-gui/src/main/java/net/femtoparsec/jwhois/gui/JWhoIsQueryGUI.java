package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.WhoIsResultHelper;
import net.femtoparsec.jwhois.utils.Utf8ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

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
public class JWhoIsQueryGUI extends JPanel implements ActionListener, KeyListener, SourceResultListener {

    private final ResourceBundle resourceBundle;

    private final JWhoIsGUIModel model;

    private final JTextField queryField;

    private final JButton queryButton;


    public static final String QUERY_FIELD_ACTION_NAME = "queryField";

    public static final String QUERY_BUTTON_ACTION_NAME = "queryButon";

    public JWhoIsQueryGUI(JWhoIsGUIModel<?> model) {
        Validate.notNull(model, "model");
        this.model = model;
        this.model.addSourceResultListener(this);
        this.resourceBundle = Utf8ResourceBundle.getBundle("JWhoIsQueryGUI");

        this.setLayout(new BorderLayout());

        this.queryField = new JTextField();
        queryField.setActionCommand(QUERY_FIELD_ACTION_NAME);
        queryField.addActionListener(this);
        queryField.addKeyListener(this);
        queryField.setPreferredSize(new Dimension(300,queryField.getHeight()));

        this.queryButton = new JButton(resourceBundle.getString("query.button.label"));
        queryButton.setActionCommand(QUERY_BUTTON_ACTION_NAME);
        queryButton.addActionListener(this);

        final String labelLabel = resourceBundle.getString("query.label.label");
        this.add(new JLabel(labelLabel), BorderLayout.WEST);
        this.add(this.queryField, BorderLayout.CENTER);
        this.add(this.queryButton, BorderLayout.EAST);

        this.queryField.setText(this.model.getQuery());
        this.updateButtonState();
    }

    private void updateButtonState() {
        final String query = this.queryField.getText();
        this.queryButton.setEnabled(!StringUtils.isEmpty(query));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (QUERY_FIELD_ACTION_NAME.equals(e.getActionCommand())) {
            final String query = this.queryField.getText();
            this.queryButton.setEnabled(!StringUtils.isEmpty(query));
            this.model.setQuery(query);
        }
        else if (QUERY_BUTTON_ACTION_NAME.equals(e.getActionCommand())) {
            final String query = this.queryField.getText();
            this.model.setQuery(query);

            this.queryButton.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    model.launchQuery();
                }
            }).start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSourceResult(WhoIsResultHelper sourceResults) {
        this.queryButton.setEnabled(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == queryField) {
            this.updateButtonState();
        }
    }
}
