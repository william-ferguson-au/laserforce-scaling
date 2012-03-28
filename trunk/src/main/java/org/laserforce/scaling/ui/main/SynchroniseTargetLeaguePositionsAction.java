package org.laserforce.scaling.ui.main;

import au.com.xandar.collection.ListListener;
import org.laserforce.scaling.common.FileDefinition;

import javax.swing.*;

/**
 * Listens to changes to a FileDefinitionList and updates the TargetLeaguePosition ComboBox field to reflect those changes.
 * <p/>
 * User: William
 * Date: 27/05/2010
 * Time: 9:10:10 PM
 */
final class SynchroniseTargetLeaguePositionsAction implements ListListener<FileDefinition> {

    private final JComboBox targetLeaguePositionField;

    SynchroniseTargetLeaguePositionsAction(JComboBox targetLeaguePositionField) {
        this.targetLeaguePositionField = targetLeaguePositionField;
    }

    @Override
    public void onAdd(FileDefinition fileDefinition, int indexOfAddedItem) {
        targetLeaguePositionField.addItem(fileDefinition.getLeaguePosition());
    }

    @Override
    public void onRemove(FileDefinition fileDefinition, int indexOfRemovedItem) {
        targetLeaguePositionField.removeItem(fileDefinition.getLeaguePosition());
    }
}