package org.laserforce.scaling.ui.addfile;

import au.com.xandar.swing.IconFactory;
import org.laserforce.scaling.common.FileDefinition;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.io.CsvScoreReader;
import org.laserforce.scaling.io.ScoreReader;
import org.laserforce.scaling.ui.common.ScalingPrefs;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
* Responsible for presenting the fields required to capture a new import file.
* <p/>
* User: William
* Date: 04/06/2010
* Time: 9:08:00 PM
*/
final class FieldsPanel extends JPanel {

    private final JComboBox fileFormatField;
    private final JTextField leagueNameField = new JTextField(2);
    private final JComboBox positionField;

    private File selectedFile;

    private static final class LinePanel extends JPanel {
        
        public LinePanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }

        public void addLabel(String text) {
            final JLabel label = new JLabel(text);
            add(label);
        }
    }

    public FieldsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        final ScalingPrefs scalingPrefs = new ScalingPrefs();

        final JTextField fileTextField = new JTextField(35);
        fileTextField.setEditable(false);

        fileFormatField = new JComboBox(new ScoreReader[] {
            new CsvScoreReader("Athlete name at field zero, scores start at field 4", 0, 4),
            new CsvScoreReader("Athlete name at field zero, scores start at field 5", 0, 5)
        });
        leagueNameField.setText(scalingPrefs.getLastLeague());
        positionField = new JComboBox(GamePosition.values());
        positionField.setSelectedItem(null);

        final ImageIcon fileChooserIcon = new IconFactory().createImageIcon(getClass(), "/folder-16by16.png");
        final JButton fileChooserButton = new JButton(fileChooserIcon);

        final LinePanel firstLine = new LinePanel();
        firstLine.addLabel("File: ");
        firstLine.add(fileTextField);
        firstLine.add(fileChooserButton);

        final LinePanel secondLine = new LinePanel();
        secondLine.addLabel("Format: ");
        secondLine.add(fileFormatField);

        // Capture the LeaguePosition for which the scores pertain.
        final LinePanel thirdLine = new LinePanel();
        thirdLine.addLabel("League: ");
        thirdLine.add(leagueNameField);
        thirdLine.addLabel("Position: ");
        thirdLine.add(positionField);

        add(firstLine);
        add(secondLine);
        add(thirdLine);

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setCurrentDirectory(scalingPrefs.getReadFolder());
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // Set the chosen File when the FileChooser dialog OK is clicked.
                if ("ApproveSelection".equals(e.getActionCommand())) {
                    selectedFile = fileChooser.getSelectedFile();
                    fileTextField.setText(selectedFile.getPath());

                    // Persist the read folder so it will be defaulted next time.
                    scalingPrefs.setReadFolder(fileChooser.getCurrentDirectory());
                    scalingPrefs.flush();
                } else {
                    selectedFile = null;
                    fileTextField.setText("");
                }
            }
        });

        // Display the FileChooser dialog when the OpenFile button is clicked.
        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(FieldsPanel.this);
            }
        });

        // Persist the last entered League so it will be defaulted next time.
        leagueNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scalingPrefs.setLastLeague(leagueNameField.getText());
                scalingPrefs.flush();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scalingPrefs.setLastLeague(leagueNameField.getText());
                scalingPrefs.flush();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not relevant because a TestField can't change its style (nor would we care if it did).
            }
        });
    }

    /**
     * If validation is not passed then the user will be prompted with MessageDialogs indicating what has failed.
     *
     * @return true if all the information required to add a file has been supplied.
     */
    public boolean passesValidation() {

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "You must select a file to import", "File Selection Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (positionField.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "You must identify the Position for these scores", "No Position Provided", JOptionPane.ERROR_MESSAGE);
            positionField.grabFocus();
            return false;
        } else if (leagueNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must identify the League for these scores", "No League Provided", JOptionPane.ERROR_MESSAGE);
            leagueNameField.grabFocus();
            return false;
        }

        return true;
    }

    public FileDefinition getSelectedFileDefinition() {
        final LeaguePosition leaguePosition = new LeaguePosition(new League(leagueNameField.getText()), (GamePosition) positionField.getSelectedItem());
        return new FileDefinition(selectedFile, (ScoreReader) fileFormatField.getSelectedItem(), leaguePosition);
    }
}
