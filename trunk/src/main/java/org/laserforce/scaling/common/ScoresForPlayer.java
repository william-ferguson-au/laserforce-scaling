package org.laserforce.scaling.common;

import java.util.Collection;

/**
 * The averages for each position for a player.
 *
 * @author williamf
 */
public final class ScoresForPlayer {

	private final ValuesForPlayer<PlayerScores> values;

	public ScoresForPlayer(Athlete athlete) {
		this.values = new ValuesForPlayer<PlayerScores>(athlete); 
	}

	/**
	 * @param average	PlayerAverage to add to the averages for this player.
	 * @throws IllegalArgumentException if the supplied average is not for the same Athlete as this AveragesForPlayer, or
     *          if the Athlete already has an average for the same position as the supplied average.
	 */
	public void addScores(PlayerScores average) {
		values.addAverage(average);
	}


	public Athlete getAthlete() {
		return values.getAthlete();
	}

	public Collection<PlayerScores> getScoresForAllPositions() {
		return values.getAverages();
	}

    public PlayerScores getScoresForPosition(LeaguePosition leaguePosition) {
        return values.getAverageFor(leaguePosition);
    }

    @Override
    public String toString() {
        return "ScoresForPlayer{" + values.getAverages() + '}';
    }
}