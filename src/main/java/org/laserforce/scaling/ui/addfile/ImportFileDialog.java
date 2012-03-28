package org.laserforce.scaling.ui.addfile;

import au.com.xandar.collection.SimpleList;
import org.laserforce.scaling.common.FileDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Responsible for retrieving information about a new Scores files.
 * <p/>
 * User: William
 * Date: 21/05/2010
 * Time: 7:49:58 PM
 */
public final class ImportFileDialog extends JDialog {

    public ImportFileDialog(final SimpleList<FileDefinition> fileDefinitionList) {
        
        setLayout(new BorderLayout());
        setTitle("Select a file containing scores to scale");
        setResizable(false);
        setModal(true);
        
        final FieldsPanel fieldsPanel = new FieldsPanel();

        final ActionListener onAddButtonClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Communicate the change back to the model and have the ModelListeners notified.
                if (fieldsPanel.passesValidation()) {
                    fileDefinitionList.add(fieldsPanel.getSelectedFileDefinition());
                    ImportFileDialog.this.setVisible(false);
                }
            }
        };
        
        final ActionListener onCancelButtonClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImportFileDialog.this.setVisible(false);
            }
        };

        final ButtonsPanel buttonsPanel = new ButtonsPanel(onAddButtonClick, onCancelButtonClick);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.EAST);

        pack();
    }
}
