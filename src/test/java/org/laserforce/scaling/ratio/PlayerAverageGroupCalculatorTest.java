package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupAvg;
import au.com.xandar.statistics.GroupAvgCalculator;
import junit.framework.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerAverage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Responsible for testing the GroupAverageCalcuator.
 * 
 * @author williamf
 */
public final class PlayerAverageGroupCalculatorTest {

	private final League LEAGUE = new League("A");
	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE, GamePosition.COMMANDER);
	
	private final Athlete PLAYER_A = new Athlete("Tragedy");
	
	private final GroupAvgCalculator<PlayerAverage> calculator = new PlayerAverageGroupAvgCalculator();

    @Test
	public void getAverageFor() {
		final Collection<PlayerAverage> scores = new ArrayList<PlayerAverage>();

		// Add first score
		scores.add(new PlayerAverage(PLAYER_A, LEAGUE_A_COMMANDER, 1000, 1));
		final GroupAvg avg1 = calculator.getAverageFor(scores);
		Assert.assertEquals(1, avg1.getWeighting());
		Assert.assertEquals(1000, (int) avg1.getAverage());

		// Add 2 more scores, both the same as the first
		scores.add(new PlayerAverage(PLAYER_A, LEAGUE_A_COMMANDER, 1000, 2));
		final GroupAvg avg2 = calculator.getAverageFor(scores);
		Assert.assertEquals(3, avg2.getWeighting());
		Assert.assertEquals(1000, (int) avg2.getAverage());

		// Add 4th score (different from the other 3) - StdDev will no longer be zero.
		scores.add(new PlayerAverage(PLAYER_A, LEAGUE_A_COMMANDER, 3000, 1));
		final GroupAvg avg3 = calculator.getAverageFor(scores);
		Assert.assertEquals(4, avg3.getWeighting());
		Assert.assertEquals(1500, (int) avg3.getAverage());
	}
}
