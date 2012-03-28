package org.laserforce.scaling.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Values for each position for a player.
 *
 * @author williamf
 */
final class ValuesForPlayer<T extends PlayerPosition> {

	private final Athlete athlete;
	private final Map<LeaguePosition, T> averages = new HashMap<LeaguePosition, T>();

	public ValuesForPlayer(Athlete athlete) {
		this.athlete = athlete;
	}

	/**
	 * @param average	PlayerAverage to add to the averages for this player.
	 * @throws IllegalArgumentException if the supplied average is not for the same Athlete as this AveragesForPlayer, or
     *          if the Athlete already has an average for the same position as the supplied average.
	 */
	public final void addAverage(T average) {
		if (!athlete.equals(average.getAthlete())) {
			throw new IllegalArgumentException("Cannot add " + average + " to values for Player : " + athlete);
		}
        if (averages.containsKey(average.getLeaguePosition())) {
            throw new IllegalArgumentException("Athlete already has a value for that LeaguePosition : " + average);
        }
		averages.put(average.getLeaguePosition(), average);
	}


	public final Athlete getAthlete() {
		return athlete;
	}

	public final Collection<T> getAverages() {
		return Collections.unmodifiableCollection(averages.values());
	}

    public final T getAverageFor(LeaguePosition leaguePosition) {
        return averages.get(leaguePosition);
    }

    @Override
    public String toString() {
        return "ValuesForPlayer{" + averages.values() + '}';
    }
}