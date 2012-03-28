package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupStat;
import au.com.xandar.statistics.GroupStatCalculator;
import junit.framework.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScores;
import org.laserforce.scaling.common.PlayerScoresFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Responsible for testing the StandardStatisticsCalculator.
 *
 * @author williamf
 */
public final class PlayerScoresGroupStatCalculatorTest {

    private final PlayerScoresFactory playerScoresFactory = new PlayerScoresFactory();

	private final League LEAGUE = new League("A");
	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE, GamePosition.COMMANDER);

	private final Athlete PLAYER_A = new Athlete("Tragedy");
	private final Athlete PLAYER_B = new Athlete("WarDog");

	private final GroupStatCalculator<PlayerScores> calculator = new PlayerScoresGroupStatCalculator();

    @Test
	public void getAverageFor() {
		final Collection<PlayerScores> scores = new ArrayList<PlayerScores>();

		// Add first score
		scores.add(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {1000}));
		final GroupStat avg1 = calculator.getAverageFor(scores);
		Assert.assertEquals(1, avg1.getWeighting());
		Assert.assertEquals(1000, (int) avg1.getAverage());
		Assert.assertEquals(0, (int) avg1.getStdDeviation());

        // Add 2 more scores, both the same as the first
		scores.add(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {1000, 1000}));
		final GroupStat avg2 = calculator.getAverageFor(scores);
		Assert.assertEquals(3, avg2.getWeighting());
		Assert.assertEquals(1000, (int) avg2.getAverage());
		Assert.assertEquals(0, (int) avg2.getStdDeviation());

        // Add 4th score (different from the other 3) - StdDev will no longer be zero.
		scores.add(playerScoresFactory.create(PLAYER_B, LEAGUE_A_COMMANDER, new Integer[] {3000}));
		final GroupStat avg3 = calculator.getAverageFor(scores);
		Assert.assertEquals(4, avg3.getWeighting());
		Assert.assertEquals(1500, (int) avg3.getAverage());
		Assert.assertEquals(866, (int) avg3.getStdDeviation());
	}
}