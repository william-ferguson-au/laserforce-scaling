package org.laserforce.scaling.common;

import java.util.Collection;

/**
 * The averages for each position for a player.
 * 
 * @author williamf
 */
public final class AveragesForPlayer {

	private final ValuesForPlayer<PlayerAverage> values;

	public AveragesForPlayer(Athlete athlete) {
		values = new ValuesForPlayer<PlayerAverage>(athlete); 
	}
	
	/**
	 * @param average	PlayerAverage to add to the averages for this player.
	 * @throws IllegalArgumentException if the supplied average is not for the same Athlete as this AveragesForPlayer, or
     *          if the Athlete already has an average for the same position as the supplied average.
	 */
	public void addAverage(PlayerAverage average) {
		values.addAverage(average);
	}
	
	
	public Athlete getAthlete() {
		return values.getAthlete();
	}

	public Collection<PlayerAverage> getAverages() {
		return values.getAverages();
	}

    public PlayerAverage getAverageFor(LeaguePosition leaguePosition) {
        return values.getAverageFor(leaguePosition);
    }

    @Override
    public String toString() {
        return "AveragesForPlayer{" + values.getAverages() + '}';
    }
}
