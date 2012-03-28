package org.laserforce.scaling.common;

import org.laserforce.scaling.io.ScoreReader;

import java.io.File;

/**
 * Describes a Score File to be imported and its format.
 * <p/>
 * User: William
 * Date: 26/05/2010
 * Time: 7:09:43 PM
 */
public final class FileDefinition {

    private final File file;
    private final ScoreReader fileReader;
    private final LeaguePosition leaguePosition;

    public FileDefinition(File file, ScoreReader fileReader, LeaguePosition leaguePosition) {
        this.file = file;
        this.fileReader = fileReader;
        this.leaguePosition = leaguePosition;
    }

    public File getFile() {
        return file;
    }

    public ScoreReader getFileFormat() {
        return fileReader;
    }

    public LeaguePosition getLeaguePosition() {
        return leaguePosition;
    }
}
