package org.laserforce.scaling.ui.main;

import au.com.xandar.collection.SimpleList;
import org.laserforce.scaling.common.FileDefinition;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Responsible for displaying a window that allows a user to select score files, scaling values and to initiate the scaling and ranking of scores.
 * <p/>
 * User: William
 * Date: 20/05/2010
 * Time: 7:51:00 PM
 */
public final class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Laserforce Scaling");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setLocationByPlatform(true);

        final SimpleList<FileDefinition> fileDefinitionList = new SimpleList<FileDefinition>();

        final SettingsPanel settingsPanel = new SettingsPanel();

        final ActionListener displayAddFileDialogAction = new DisplayAddFileDialogAction(this, fileDefinitionList);
        final ActionListener scaleScoresAction = new ScaleScoresAction(fileDefinitionList, settingsPanel);
        final ButtonsPanel buttonsPanel = new ButtonsPanel(scaleScoresAction, displayAddFileDialogAction);

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(settingsPanel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        final ImportFilesPanel importFilesPanel = new ImportFilesPanel();
        final JScrollPane scrollPane = new JScrollPane(importFilesPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        pack();

        // Add FileDefinitionListListeners that update the UI to reflect changes to the FileDefinitionList.
        fileDefinitionList.addListListener(new SynchroniseImportFilesAction(importFilesPanel, fileDefinitionList));
        fileDefinitionList.addListListener(new SynchroniseTargetLeaguePositionsAction(settingsPanel.targetLeaguePositionField));
    }

    private static class ButtonsPanel extends JPanel {

        public ButtonsPanel(ActionListener scaleScoresAction, ActionListener displayAddFileAction) {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(3, 3, 3, 3));

            final JButton scaleButton = new JButton("Scale");
            final JButton addFileButton = new JButton("Add File");

            add(scaleButton, BorderLayout.NORTH);
            add(addFileButton, BorderLayout.SOUTH);

            addFileButton.addActionListener(displayAddFileAction);
            scaleButton.addActionListener(scaleScoresAction);
        }
    }
    
    public static void main(String[] args) {
        final MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}
