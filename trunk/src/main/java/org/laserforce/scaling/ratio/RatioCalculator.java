package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupStat;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.ScoresByPlayer;

import java.util.HashSet;
import java.util.Set;

final class RatioCalculator {

	private final int minNrGames;
	
	/**
	 * @param minNrGames	Minimum number of games that an Athlete must have played in a position 
	 * 						for those scores to be included in the ratio calculation.
	 */
	public RatioCalculator(int minNrGames) {
		this.minNrGames = minNrGames;
	}

	/**
	 * Calculates the ratios for each pair of LeaguePositionScores.
	 * <p>
	 * If there are no Athletes with scores in common between 2 LeaguePositions,
	 * then the ratio for that LeaguePosition pair with have a weighting of zero games.
	 * </p>
	 * 
	 * @param scoresByPlayer	Averages for every LeaguePosition for every Athlete.
	 * @return RatiosBetweenPosition for each pair of LeaguePositionScores.
	 */
	public RatiosBetweenPosition calculateRatios(ScoresByPlayer scoresByPlayer) {
		
		final RatiosBetweenPosition ratios = new RatiosBetweenPosition();
		final ScoresByPosition scoresByPosition = ScoresByPosition.createFrom(scoresByPlayer, minNrGames);
		
		for (final LeaguePosition fromPosition : scoresByPosition.getLeaguePositions()) {
			final LeaguePositionScores fromPositionAverage = scoresByPosition.getLeaguePositionScores(fromPosition);
			
			// Looping on scoresByPosition.getLeaguePositions() ensures that we calculate ratios between every pair of LeaguePosition that we have averages for.
			// However, the calculated ratio will not be able to perform any transformation unless there are players that have played both positions.  
			for (final LeaguePosition targetPosition : scoresByPosition.getLeaguePositions()) {
				final LeaguePositionScores targetPositionAverage = scoresByPosition.getLeaguePositionScores(targetPosition);
				ratios.add(calculateRatioBetween(fromPositionAverage, targetPositionAverage));
			}
		}
		
		return ratios;
	}

    /**
     * Creates a LeaguePositionRatio from one LeaguePositionScores to another,
     * considering only the scores for those athletes that have competed in both positions.
     *
     * @param fromScores	LeaguePositionScores from which to base the ratio.
     * @param targetAverage	LeaguePositionScores for the target position.
     * @return LeaguePositionRatio that compares the fromScores to the targetAverage.
     * 		If there are no scores in common between the LeaguePositions then a ratio with zero games will be returned.
     */
    public LeaguePositionRatio calculateRatioBetween(LeaguePositionScores fromScores, LeaguePositionScores targetAverage) {

        // Determine which Athletes played in both positions (ie the intersection).
        final Set<Athlete> athletes = new HashSet<Athlete>();
        athletes.addAll(fromScores.getAthletes());
        athletes.retainAll(targetAverage.getAthletes());

        // Get the averages over those players.
        final GroupStat fromGroupAverage = fromScores.getAverageFor(athletes);
        final GroupStat targetGroupAverage = targetAverage.getAverageFor(athletes);

        // Determine the ratio based upon those averages.
        final int nrGames = Math.min(fromGroupAverage.getWeighting(), targetGroupAverage.getWeighting());

        return new LeaguePositionRatio(
                fromScores.getLeaguePosition(), targetAverage.getLeaguePosition(),
                fromGroupAverage.getAverage(), targetGroupAverage.getAverage(),
                fromGroupAverage.getStdDeviation(), targetGroupAverage.getStdDeviation(),
                nrGames
            );
    }


}
