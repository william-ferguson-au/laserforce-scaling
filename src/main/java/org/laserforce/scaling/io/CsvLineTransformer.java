package org.laserforce.scaling.io;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Transforms a series of values representing an Athlete and their scores in a position into a PlayerAverage.
 *
 * @author williamf
 */
final class CsvLineTransformer {

	private final int athleteNameColumn;
	private final int firstScoreColumn;

    public CsvLineTransformer(int athleteNameColumn, int firstScoreColumn) {
        this.athleteNameColumn = athleteNameColumn;
        this.firstScoreColumn = firstScoreColumn;
	}

    /**
     * Create Collection of Score from the current line.
     *
     * @param line	Array of CSV cells for the current line representing a player and her scores.
     * @return Collection of score (as Double).
     */
    public String getAthleteName(String[] line) {
        return line[athleteNameColumn];
    }

	/**
	 * Create Collection of Score from the current line.
	 *
	 * @param line	Array of CSV cells for the current line representing a player and her scores.
	 * @return Collection of score (as Double).
	 */
	public Collection<Integer> getScores(String[] line) {
		final Collection<Integer> scores = new ArrayList<Integer>();
		for (int i = firstScoreColumn; i < line.length; i++) {
			if (line[i] == null) {
				continue;
			}
            if (line[i].isEmpty()) {
                continue;
            }
			final Integer score = Integer.valueOf(line[i]);
			if (score != null) {
				scores.add(score);
			}
		}

		return scores;
	}
}