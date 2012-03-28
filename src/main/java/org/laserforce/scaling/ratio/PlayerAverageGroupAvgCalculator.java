package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupAvg;
import au.com.xandar.statistics.GroupAvgCalculator;
import org.laserforce.scaling.common.PlayerAverage;

import java.util.Collection;

/**
 * Calculates a GroupAvg for a Collection of PlayerAverage.
 *
 * @author williamf
 */
final class PlayerAverageGroupAvgCalculator implements GroupAvgCalculator<PlayerAverage> {

	/**
	 * @param scores	Collection of PlayerAverage for which to calculate the GroupAvg.
	 * @return GroupAverage of the provide scores.
	 */
	public GroupAvg getAverageFor(Collection<PlayerAverage> scores) {
		
		double totalScore = 0;
		int nrGames = 0;

		for (final PlayerAverage playerAverage : scores) {
			totalScore += (playerAverage.getAverageScore() * playerAverage.getNrGames());
			nrGames += playerAverage.getNrGames();
		}
		
		final double avgScore = (nrGames == 0) ? 0 : totalScore / nrGames;
		
		return new GroupAvg(avgScore, nrGames);
	}
}