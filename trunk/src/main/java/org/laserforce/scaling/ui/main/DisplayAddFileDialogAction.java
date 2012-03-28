package org.laserforce.scaling.ui.main;

import au.com.xandar.collection.SimpleList;
import org.laserforce.scaling.common.FileDefinition;
import org.laserforce.scaling.ui.addfile.ImportFileDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays the ScoresDialog when notified.
 * <p/>
 * User: William
 * Date: 27/05/2010
 * Time: 8:55:51 PM
 */
final class DisplayAddFileDialogAction implements ActionListener {

    private final Component owner;
    private final SimpleList<FileDefinition> fileDefinitionList;

    DisplayAddFileDialogAction(Component owner, SimpleList<FileDefinition> fileDefinitionList) {
        this.owner = owner;
        this.fileDefinitionList = fileDefinitionList;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final ImportFileDialog getScoresDialog = new ImportFileDialog(fileDefinitionList);
        getScoresDialog.setLocationRelativeTo(owner);
        getScoresDialog.setVisible(true);
    }
}

