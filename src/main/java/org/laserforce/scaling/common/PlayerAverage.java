package org.laserforce.scaling.common;

/**
 * Represents the average score for a player in one LeaguePosition.
 * 
 * @author williamf
 */
public final class PlayerAverage implements PlayerPosition {

	private final Athlete athlete;
	private final LeaguePosition leaguePosition;
	private final double averageScore;
	private final int nrGames;

	public PlayerAverage(Athlete athlete, LeaguePosition position, double avgScore, int nrGames) {
		this.athlete = athlete;
		this.leaguePosition = position;
		this.averageScore = avgScore;
		this.nrGames = nrGames;
	}

	public Athlete getAthlete() {
		return athlete;
	}

	public LeaguePosition getLeaguePosition() {
		return leaguePosition;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public int getNrGames() {
		return nrGames;
	}

	@Override
	public String toString() {
		return "TransformedAverage [athlete=" + athlete + ", averageScore=" + averageScore + ", leaguePosition=" + leaguePosition + ", nrGames=" + nrGames + "]";
	}
}