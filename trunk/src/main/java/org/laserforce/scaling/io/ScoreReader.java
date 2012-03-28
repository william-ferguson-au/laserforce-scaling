package org.laserforce.scaling.io;

import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.ScoresByPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Reads a File of scores for a LeaguePosition and converts it into {@link org.laserforce.scaling.common.ScoresByPlayer}.
 * 
 * @author williamf
 */
public interface ScoreReader {

    /**
     * Reads the File and converts it into ScoresByPlayer for the configured LeaguePosition.
     *
     * @param file              CSV File containing the scores for each athlete to be read.
     * @param leaguePosition    LeaguePosition to which to attribute the read averages.
     * @return AveragesByPlayer for all players contained in the supplied file.
     * @throws IOException if the File does not exist or there are duplicate records for an athlete.
     */
	public ScoresByPlayer read(File file, LeaguePosition leaguePosition) throws IOException;
}
