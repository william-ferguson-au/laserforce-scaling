package org.laserforce.scaling.io;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScores;
import org.laserforce.scaling.common.ScoresByPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Reads a CSV File of scores for a LeaguePosition and converts it into {@link org.laserforce.scaling.common.ScoresByPlayer}.
 *
 * @author williamf
 */
public final class CsvScoreReader implements ScoreReader {

	private static final Logger LOGGER = Logger.getLogger(CsvScoreReader.class);

    private final String description;
    private final CsvLineTransformer transformer;

    public CsvScoreReader(String description, int athleteNameColumn, int firstScoreColumn) {
        this.description = description;
        this.transformer = new CsvLineTransformer(athleteNameColumn, firstScoreColumn);
    }

    /**
     * Reads the CSV FIle and converts it into ScoresByPlayer for the configured LeaguePosition.
     *
     * @param file              CSV File containing the scores for each athlete to be read.
     * @param leaguePosition    LeaguePosition to which to attribute the read averages.
     * @return AveragesByPlayer for all players contained in the supplied file.
     * @throws java.io.IOException if the File does not exist or there are duplicate records for an athlete.
     */
    @Override
	public ScoresByPlayer read(File file, LeaguePosition leaguePosition) throws IOException {

		final ScoresByPlayer scoresByPlayer = new ScoresByPlayer();

		// Open file
		final CSVReader reader = new CSVReader(new FileReader(file));

		// Read first line
		final String[] firstLine = reader.readNext();
		LOGGER.debug("Read first line: " + Arrays.asList(firstLine));

		String[] line;
		while ((line = reader.readNext()) != null) {

            if ((line[0] == null) || "".equals(line[0])) {
                LOGGER.debug("Read (and ignored) line: " + Arrays.asList(line));
                continue; // empty line so ignore it.
            }

            LOGGER.debug("Read line: " + Arrays.asList(line));

            final Athlete athlete = new Athlete(transformer.getAthleteName(line));
            final Collection<Integer> scores = transformer.getScores(line);
			final PlayerScores playerPositionScores = new PlayerScores(athlete, leaguePosition, scores);

			// Exception if already exists
			if (scoresByPlayer.hasScoresFor(playerPositionScores.getAthlete(), playerPositionScores.getLeaguePosition())) {
				throw new IOException("Duplicate set of averages for Athlete : " + playerPositionScores.getAthlete());
			}

			LOGGER.debug("  transformed to : " + playerPositionScores);
			scoresByPlayer.addPlayerScores(playerPositionScores);
		}
		reader.close();

		return scoresByPlayer;
	}

    @Override
    public String toString() {
        return description;
    }
}