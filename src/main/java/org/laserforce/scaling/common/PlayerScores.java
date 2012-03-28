package org.laserforce.scaling.common;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a Collection of scores for a player in one LeaguePosition.
 *
 * @author williamf
 */
public final class PlayerScores implements PlayerPosition {

	private final Athlete athlete;
	private final LeaguePosition leaguePosition;
	private final Collection<Integer> scores;

	/**
	 *
	 * @param athlete	Athlete whose scores have been aggregated.
	 * @param position	LeaguePosition for which the scores have been aggregated.
	 * @param scores	Collection of scores by this Athlete in this LeaguePosition.
	 * 					They are used to determine the standard deviation of all the scores that will be transformed from one position to another.
	 */
	public PlayerScores(Athlete athlete, LeaguePosition position, Collection<Integer> scores) {
		this.athlete = athlete;
		this.leaguePosition = position;
		this.scores = Collections.unmodifiableCollection(scores);
	}

	public Athlete getAthlete() {
		return athlete;
	}

	public LeaguePosition getLeaguePosition() {
		return leaguePosition;
	}

	public int getNrGames() {
        // NB #getWeighting is required so that we can filter out player record with too few games prior to calculating ratios.
        // See ScoresByPosition#createFrom(ScoresByPlayer scoresByPlayer, int minNrGames) 
		return scores.size();
	}

	public Collection<Integer> getScores() {
		return scores;
	}

	@Override
	public String toString() {
		return "PlayerScores [athlete=" + athlete + ", leaguePosition=" + leaguePosition + ", nrGames=" + scores.size() + ", scores=" + scores + "]";
	}
}