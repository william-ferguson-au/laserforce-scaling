package org.laserforce.scaling.ratio;

import junit.framework.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerAverage;

/**
 * Responsible for testing LeaguePositionRatio.
 * 
 * @author williamf
 */
public final class LeaguePositionRatioTest {

	private final League LEAGUE = new League("A");
	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE, GamePosition.COMMANDER);
	private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(LEAGUE, GamePosition.AMMO);
	
	private final Athlete PLAYER_A = new Athlete("Tragedy");

    @Test
	public void transform() {
		
		final PlayerAverage commanderScore = new PlayerAverage(PLAYER_A, LEAGUE_A_COMMANDER, 12000, 10);
		final PlayerAverage ammoScore = new PlayerAverage(PLAYER_A, LEAGUE_A_AMMO, 6000, 4);
        
		final LeaguePositionRatio commanderToAmmoRatio = new LeaguePositionRatio(LEAGUE_A_COMMANDER, LEAGUE_A_AMMO, 11333, 5000, 942, 1000, 8);

		// Validate that we can only transform scores with an appropriate ratio.
		try {
			commanderToAmmoRatio.transform(ammoScore);
			Assert.fail("Should not be able to transformed a LeaguePosition using a LeaguePositionRatio that does not have that LeaguePosition as it's from position");
		} catch (IllegalArgumentException e) {
			// as expected
		}

		// Validate a standard transform.
		final PlayerAverage transformedCommanderScore = commanderToAmmoRatio.transform(commanderScore);
		Assert.assertEquals(LEAGUE_A_AMMO, transformedCommanderScore.getLeaguePosition());
		Assert.assertEquals(PLAYER_A, transformedCommanderScore.getAthlete());
		Assert.assertEquals(5708, transformedCommanderScore.getAverageScore(), 1.0);
		Assert.assertEquals(8, transformedCommanderScore.getNrGames());
		
		// Validate that we can transform a position to itself and that it shouldn't change.
		final LeaguePositionRatio commanderToCommanderRatio = new LeaguePositionRatio(LEAGUE_A_COMMANDER, LEAGUE_A_COMMANDER, 11333, 11333, 942, 942, 15);

		final PlayerAverage transformedScore = commanderToCommanderRatio.transform(commanderScore);
		Assert.assertEquals(LEAGUE_A_COMMANDER, transformedScore.getLeaguePosition());
		Assert.assertEquals(PLAYER_A, transformedScore.getAthlete());
		Assert.assertEquals(12000, transformedScore.getAverageScore(), 1.0);
		Assert.assertEquals(10, transformedScore.getNrGames());
		
	}
}
