package org.laserforce.scaling.ui.main;

import javax.swing.*;
import java.awt.*;

/**
* Responsible for managing a list of FileDefinitionPanel and displaying them in a vertical layout.
* <p/>
* User: William
* Date: 27/05/2010
* Time: 9:17:36 PM
*/
final class ImportFilesPanel extends JPanel {

    public ImportFilesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.WHITE);
        add(Box.createVerticalGlue()); // Always have extra space at the bottom.
    }

    public void addFileDefinitionPanel(FileDefinitionPanel fileDefinitionPanel) {
        add(fileDefinitionPanel, getComponentCount() - 1);
        revalidate();
        repaint();
    }

    public void removeFileDefinitionPanel(int indexOfPanel) {
        remove(indexOfPanel);
        revalidate();
        repaint();
    }
}
