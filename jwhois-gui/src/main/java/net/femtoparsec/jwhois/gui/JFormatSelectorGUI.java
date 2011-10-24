package net.femtoparsec.jwhois.gui;

import net.femtoparsec.jwhois.Format;
import net.femtoparsec.jwhois.utils.Utf8ResourceBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * User: Bastien Aracil
 * Date: 24/10/11
 */
public class JFormatSelectorGUI extends JPanel implements ActionListener {

    private final ResourceBundle resourceBundle;

    private final JWhoIsGUIModel<?> model;

    public JFormatSelectorGUI(JWhoIsGUIModel<?> model) {
        this.model = model;
        this.resourceBundle = Utf8ResourceBundle.getBundle("JFormatSelectorGUI");

        final Format[] formats = {Format.RPSL, Format.XML, Format.JSON};

        this.setLayout(new FlowLayout());
        this.add(new JLabel(resourceBundle.getString("label.label")));

        ButtonGroup group = new ButtonGroup();
        for (Format format : formats) {
            JRadioButton button = new JRadioButton(format.name());
            group.add(button);
            button.setSelected(format == model.getPreferredFormat());
            button.setActionCommand(format.name());
            button.addActionListener(this);
            this.add(button);
        }
        new JRadioButton();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Format format = Format.valueOf(e.getActionCommand());
        this.model.setPreferredFormat(format);
    }
}
