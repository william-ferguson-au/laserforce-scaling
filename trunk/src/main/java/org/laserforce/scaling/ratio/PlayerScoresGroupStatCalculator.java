package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupAvg;
import au.com.xandar.statistics.GroupAvgCalculator;
import au.com.xandar.statistics.GroupStat;
import au.com.xandar.statistics.GroupStatCalculator;
import org.laserforce.scaling.common.PlayerAverage;
import org.laserforce.scaling.common.PlayerScores;

import java.util.Collection;

/**
 * Calculates a GroupStat for a Collection of PlayerAverage.
 * <p>
 * Calculates the Mean and Count from {@link PlayerAverage#getAverageScore()} and {@link PlayerAverage#getNrGames()}.
 * </p>
 * <p>
 * Calculates the StdDev by summing the square of the differences between the mean and the value of every individual score for a player.
 * ie StdDev = Sqrt(1/n * sum((X - u) ^ 2)
 * </p>
 *
 * @author williamf
 */
final class PlayerScoresGroupStatCalculator implements GroupStatCalculator<PlayerScores> {

    private final GroupAvgCalculator<PlayerScores> groupAvgCalculator = new GroupAvgCalculator<PlayerScores>() {

        /**
         * @param scores	Collection of PlayerScores for which to calculate the GroupAvg.
         * @return GroupAverage of the provide scores.
         */
        public GroupAvg getAverageFor(Collection<PlayerScores> scores) {

            double totalScore = 0;
            int nrGames = 0;

            for (final PlayerScores playerScores : scores) {
                for (final Integer score : playerScores.getScores()) {
                    totalScore += score;
                    nrGames++;
                }
            }

            final double avgScore = (nrGames == 0) ? 0 : totalScore / nrGames;

            return new GroupAvg(avgScore, nrGames);
        }
    };


	/**
	 * @param scores			Collection of PlayerAverage for which to calculate the GroupStat.
	 * @return GroupAverage of the provide scores.
	 */
	public GroupStat getAverageFor(Collection<PlayerScores> scores) {

        final GroupAvg groupAvg = groupAvgCalculator.getAverageFor(scores);

        double sumTotal = 0;
        for (final PlayerScores playerScore : scores) {
        	for (final Integer score: playerScore.getScores()) {
                sumTotal += Math.pow(score - groupAvg.getAverage(), 2);
        	}
        }
        final double stdDeviation = (groupAvg.getWeighting() == 0) ? 0 : Math.sqrt(sumTotal / groupAvg.getWeighting());

		return new GroupStat(groupAvg.getAverage(), stdDeviation, groupAvg.getWeighting());
	}

}