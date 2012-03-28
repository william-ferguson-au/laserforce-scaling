package org.laserforce.scaling.ui.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

final class SettingsPanel extends JPanel {

    private static final int NR_TEXT_COLUMNS = 3;
    final JTextField minNrGamesField = new JTextField("3", NR_TEXT_COLUMNS);
    final JTextField minLevelField = new JTextField("1", NR_TEXT_COLUMNS);
    final JTextField maxLevelField = new JTextField("12", NR_TEXT_COLUMNS);
    final JCheckBox printTransformedPositionScoresField = new JCheckBox();

    final JComboBox targetLeaguePositionField = new JComboBox();

    public SettingsPanel() {
        setLayout(new GridLayout(3, 4, 15, 0));
        setBorder(new EmptyBorder(3, 3, 3, 3));

        targetLeaguePositionField.setPreferredSize(new Dimension(150, 20));

        add(new JLabel("Min Nr Games:"));
        add(new WesternPanel(minNrGamesField));
        add(new JLabel("Target League Position:"));
        add(new WesternPanel(targetLeaguePositionField));

        add(new JLabel("Min LeagueLevel:"));
        add(new WesternPanel(minLevelField));
        add(new JLabel("Show Intermediate Scores:"));
        add(printTransformedPositionScoresField);

        add(new JLabel("Max LeagueLevel:"));
        add(new WesternPanel(maxLevelField));
        add(new JLabel()); // Padding
        add(new JLabel());
    }

    /**
     * JPanel that puts its one component on the West of a BorderLayout. 
     */
    private static class WesternPanel extends JPanel {
        WesternPanel(JComponent field) {
            setLayout(new BorderLayout());
            add(field, BorderLayout.WEST);
        }
    }
}