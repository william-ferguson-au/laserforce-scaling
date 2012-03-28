package org.laserforce.scaling.ui.common;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Responsible for managing the preferences used by the scaling application.
 * <p/>
 * User: William
 * Date: 09/06/2010
 * Time: 8:00:07 PM
 */
public final class ScalingPrefs {

    private static final Logger LOGGER = Logger.getLogger(ScalingPrefs.class);
    
    private static final String READ_FOLDER_KEY = "ReadFolder";     // The folder from which the score files are imported
    private static final String WRITE_FOLDER_KEY = "WriteFolder";   // The folder at which the output is written.
    private static final String LAST_LEAGUE_KEY = "LastLeague";     // The last League file imported by the UI.

    private final Preferences prefs;
    private final String defaultFolder;

    public ScalingPrefs() {
        final Preferences userRootPrefs = Preferences.userRoot();
        prefs = userRootPrefs.node("ScalingPrefs");
        defaultFolder = System.getProperties().getProperty("user.home");
    }

    public File getReadFolder() {
        return new File(prefs.get(READ_FOLDER_KEY, defaultFolder));
    }

    public void setReadFolder(File readFolder) {
        prefs.put(READ_FOLDER_KEY, readFolder.getPath());
    }

    public File getWriteFolder() {
        return new File(prefs.get(WRITE_FOLDER_KEY, defaultFolder));
    }

    public void setWriteFolder(File readFolder) {
        prefs.put(WRITE_FOLDER_KEY, readFolder.getPath());
    }

    public String getLastLeague() {
        return prefs.get(LAST_LEAGUE_KEY, "A");
    }

    public void setLastLeague(String lastLeague) {
        prefs.put(LAST_LEAGUE_KEY, lastLeague);
    }

    public void flush() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            LOGGER.warn("Could not save ScalingPrefs", e);
        }
    }
}
