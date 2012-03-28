package org.laserforce.scaling.ui.main;

import au.com.xandar.collection.ListListener;
import au.com.xandar.collection.SimpleList;
import org.laserforce.scaling.common.FileDefinition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listens to changes to a FileDefinitionList and updates the ImportFilesPanel to reflect those changes.
 * <p/>
 * User: William
 * Date: 27/05/2010
 * Time: 9:10:10 PM
 */
final class SynchroniseImportFilesAction implements ListListener<FileDefinition> {

    private final ImportFilesPanel importFilesPanel;
    private final SimpleList<FileDefinition> fileDefinitionList;

    SynchroniseImportFilesAction(ImportFilesPanel importFilesPanel, SimpleList<FileDefinition> fileDefinitionList) {
        this.importFilesPanel = importFilesPanel;
        this.fileDefinitionList = fileDefinitionList;
    }

    @Override
    public void onAdd(final FileDefinition fileDefinition, int indexOfAddedItem) {

        final ActionListener removeFileDefinitionAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileDefinitionList.remove(fileDefinition);
            }
        };

        final FileDefinitionPanel fileDefinitionPanel = new FileDefinitionPanel(removeFileDefinitionAction, fileDefinition);
        importFilesPanel.addFileDefinitionPanel(fileDefinitionPanel);
    }

    @Override
    public void onRemove(FileDefinition fileDefinition, int indexOfRemovedItem) {
        importFilesPanel.removeFileDefinitionPanel(indexOfRemovedItem);
    }
}

