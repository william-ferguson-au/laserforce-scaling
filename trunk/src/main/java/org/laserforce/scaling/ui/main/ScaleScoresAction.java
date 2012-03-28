package org.laserforce.scaling.ui.main;

import au.com.xandar.collection.SimpleList;
import org.apache.log4j.Logger;
import org.laserforce.scaling.common.FileDefinition;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.ScaledResult;
import org.laserforce.scaling.common.ScoresByPlayer;
import org.laserforce.scaling.io.ScaledResultPrinter;
import org.laserforce.scaling.io.ScoreReader;
import org.laserforce.scaling.ratio.PlayerRanker;
import org.laserforce.scaling.ratio.Scaler;
import org.laserforce.scaling.ui.common.ScalingPrefs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Scales the Scores.
 * <p/>
 * User: William
 * Date: 27/05/2010
 * Time: 8:55:51 PM
 */
final class ScaleScoresAction implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(ScaleScoresAction.class);
    
    private static final String VALIDATION_FAILURE_TITLE = "Cannot scale scores";
    
    private final SimpleList<FileDefinition> fileDefinitions;
    private final JTextField minNrGamesField;
    private final JTextField minLevelField;
    private final JTextField maxLevelField;
    private final JComboBox targetLeaguePositionField;
    private final JCheckBox printTransformedPositionScoresField;
    private final JFileChooser fileChooser = new JFileChooser();
    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");

    private File selectedFile;

    ScaleScoresAction(SimpleList<FileDefinition> fileDefinitions, SettingsPanel settingsPanel) {
        this.fileDefinitions = fileDefinitions;
        minNrGamesField = settingsPanel.minNrGamesField;
        minLevelField = settingsPanel.minLevelField;
        maxLevelField = settingsPanel.maxLevelField;
        targetLeaguePositionField = settingsPanel.targetLeaguePositionField;
        printTransformedPositionScoresField = settingsPanel.printTransformedPositionScoresField;

        final ScalingPrefs scalingPrefs = new ScalingPrefs(); 
        fileChooser.setCurrentDirectory(scalingPrefs.getWriteFolder());
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ApproveSelection".equals(e.getActionCommand())) {
                    selectedFile = fileChooser.getSelectedFile();

                    // Save any changes to the write folder into the preferences.
                    scalingPrefs.setWriteFolder(fileChooser.getCurrentDirectory());
                    scalingPrefs.flush();
                } else {
                    selectedFile = null;
                }
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        // Check to see if past DateOfNoReturn
        final Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 9, 10);
        if (new Date().after(calendar.getTime())) {
            JOptionPane.showMessageDialog((Component) event.getSource(), new String[] {"Sorry, this version is only valid for a limited time.", "If the Brisbane Laserforce League adopts it as the standard mechanism for determining League Levels then I will make an unrestricted version available.", "Otherwise I'll use it myself to pick the best damn team available.", "     WarDog"}, "Licence has expired", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate minNrGames.
        final int minNrGames;
        try {
            minNrGames = Integer.parseInt(minNrGamesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog((Component) event.getSource(), "Invalid minimum number of games : " + e.getMessage(), VALIDATION_FAILURE_TITLE, JOptionPane.ERROR_MESSAGE);
            minNrGamesField.setRequestFocusEnabled(true);
            return;
        }

        // Validate targetLeaguePosition.
        final LeaguePosition targetLeaguePosition = (LeaguePosition) targetLeaguePositionField.getSelectedItem();
        if (targetLeaguePosition == null) {
            JOptionPane.showMessageDialog((Component) event.getSource(), "You must select a target LeaguePosition", VALIDATION_FAILURE_TITLE, JOptionPane.ERROR_MESSAGE);
            targetLeaguePositionField.setRequestFocusEnabled(true);
            return;
        }
        
        // Validate min level for ranking.
        final int minLevel;
        try {
            minLevel = Integer.parseInt(minLevelField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog((Component) event.getSource(), "Invalid minimum level : " + e.getMessage(), VALIDATION_FAILURE_TITLE, JOptionPane.ERROR_MESSAGE);
            minLevelField.setRequestFocusEnabled(true);
            return;
        }

        // Validate max level for ranking.
        final int maxLevel;
        try {
            maxLevel = Integer.parseInt(maxLevelField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog((Component) event.getSource(), "Invalid maximum level : " + e.getMessage(), VALIDATION_FAILURE_TITLE, JOptionPane.ERROR_MESSAGE);
            maxLevelField.setRequestFocusEnabled(true);
            return;
        }

        // Ask for the File to which to output the results.
        final File defaultFile = new File(fileChooser.getCurrentDirectory(), targetLeaguePosition.toString() + "-" + dateFormat.format(new Date()) + ".txt");
        fileChooser.setSelectedFile(defaultFile);
        fileChooser.showSaveDialog((Component) event.getSource());
        if (selectedFile == null) {
            return; // No file selected.
        }

        // Read all the FileDefinitions.
        final ScoresByPlayer allScores = new ScoresByPlayer();
        for (final FileDefinition fileDefinition : fileDefinitions) {
            final ScoreReader reader = fileDefinition.getFileFormat();
            try {
                final ScoresByPlayer scoresForOnePosition = reader.read(fileDefinition.getFile(), fileDefinition.getLeaguePosition());
                allScores.addAverages(scoresForOnePosition);
                LOGGER.info("Loaded " + fileDefinition.getFile() + " for " + fileDefinition.getLeaguePosition());
            } catch (IOException e) {
                LOGGER.error("Failed to import : " + fileDefinition.getFile(), e);
                if (JOptionPane.showConfirmDialog((Component) event.getSource(), "Failed to import " + fileDefinition.getFile() + ".\nError: " + e.getMessage() + "\n\nProcess remaining files or cancel", "Import failed", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
        }

        // Scale the scores.
        final Scaler scaler = new Scaler();
        scaler.setRanker(new PlayerRanker(minLevel, maxLevel));
        scaler.setMinNrGamesToConsider(minNrGames);
        
        final ScaledResult scaledResult = scaler.scale(allScores, targetLeaguePosition);
        scaledResult.setFileDefinitions(fileDefinitions);

        // Output the Ranked players.
        try {
            final PrintStream outputStream = new PrintStream(selectedFile);
            final ScaledResultPrinter printer = new ScaledResultPrinter(outputStream);
            printer.setPrintScaledPositionScores(printTransformedPositionScoresField.isSelected());
            printer.print(scaledResult);
            outputStream.close();

            LOGGER.info("Scores scaled and saved to " + selectedFile);

            // Provide confirm on successful scale.
            JOptionPane.showMessageDialog((Component) event.getSource(), "Scores scaled and saved to " + selectedFile);
        } catch (IOException e) {
            LOGGER.error("Scaling failed", e);
            JOptionPane.showMessageDialog((Component) event.getSource(), e.getMessage(), "Scaling failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}