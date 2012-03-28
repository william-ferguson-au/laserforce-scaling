package org.laserforce.scaling.ui.main;

import au.com.xandar.swing.IconFactory;
import org.laserforce.scaling.common.FileDefinition;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
* Responsible for presenting what files have been selected for import.
* <p/>
* User: William
* Date: 27/05/2010
* Time: 9:17:15 PM
*/
final class FileDefinitionPanel extends JPanel {

    private static final ImageIcon REMOVE_ICON = new IconFactory().createImageIcon(FileDefinitionPanel.class, "/process-stop-16by16.png");

    public FileDefinitionPanel(ActionListener removeFileDefinitionAction, final FileDefinition fileDefinition) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        setBackground(Color.BLUE);

        final JTextField fileTextField = new JTextField();
        fileTextField.setEditable(false);
        fileTextField.setText(fileDefinition.getFile().getPath());

        final JTextField leaguePositionTextField = new JTextField(8);
        leaguePositionTextField.setEditable(false);
        leaguePositionTextField.setText(fileDefinition.getLeaguePosition().toString());
        leaguePositionTextField.setMaximumSize(new Dimension(50, Short.MAX_VALUE));

        final JTextField formatTextField = new JTextField();
        formatTextField.setEditable(false);
        formatTextField.setText(fileDefinition.getFileFormat().toString());
        formatTextField.setMaximumSize(new Dimension(110, Short.MAX_VALUE));

        final JButton removeButton = new JButton(REMOVE_ICON);
        removeButton.setPreferredSize(new Dimension(20, 20));

        add(removeButton);
        add(Box.createRigidArea(new Dimension(2, 0)));
        add(leaguePositionTextField);
        add(Box.createRigidArea(new Dimension(2, 0)));
        add(formatTextField);
        add(Box.createRigidArea(new Dimension(2, 0)));
        add(fileTextField);

        removeButton.addActionListener(removeFileDefinitionAction);
    }
}
