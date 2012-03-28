package org.laserforce.scaling.ui.addfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
* Responsible for presenting Add and Cancel buttons.
* <p/>
* User: William
* Date: 04/06/2010
* Time: 9:07:45 PM
*/
final class ButtonsPanel extends JPanel {

    public ButtonsPanel(ActionListener onAddButtonClick, ActionListener onCancelButtonClick) {
        
        final JButton addButton = new JButton("Add");
        final JButton cancelButton = new JButton("Cancel");

        addButton.setSize(50, 10);
        cancelButton.setSize(50, 10);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(3, 3, 3, 3));
        add(addButton, BorderLayout.NORTH);
        add(cancelButton, BorderLayout.SOUTH);

        addButton.addActionListener(onAddButtonClick);
        cancelButton.addActionListener(onCancelButtonClick);
    }
}
