package org.laserforce.scaling.ratio;

import junit.framework.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScoresFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for testing the LeaguePositionScores.
 * 
 * @author williamf
 */
public final class LeaguePositionAverageTest {

    private final PlayerScoresFactory playerScoresFactory = new PlayerScoresFactory();

	private final League LEAGUE = new League("A");
	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE, GamePosition.COMMANDER);
	private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(LEAGUE, GamePosition.AMMO);

	private final Athlete PLAYER_A = new Athlete("Tragedy");
	private final Athlete PLAYER_B = new Athlete("WarDog");
	
    private final Set<Athlete> PLAYER_A_ONLY = new HashSet<Athlete>(Arrays.asList(PLAYER_A));
	private final Set<Athlete> PLAYER_A_AND_B = new HashSet<Athlete>(Arrays.asList(PLAYER_A, PLAYER_B));

    @Test
	public void testGetLeagueGamePosition() {
		final LeaguePositionScores commanderScores = new LeaguePositionScores(LEAGUE_A_COMMANDER);
		Assert.assertEquals(LEAGUE_A_COMMANDER, commanderScores.getLeaguePosition());
		
		final LeaguePositionScores ammoScores = new LeaguePositionScores(LEAGUE_A_AMMO);
		Assert.assertEquals(LEAGUE_A_AMMO, ammoScores.getLeaguePosition());
	}
	
	@Test
	public void add() {
		final LeaguePositionScores commanderScores = new LeaguePositionScores(LEAGUE_A_COMMANDER);

        // Should be able to add a CommanderScore to a CommanderScoreAverage.
		commanderScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {1000}));
		
		try {
			commanderScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {1000}));
			Assert.fail("Should not be able to add an Ammo score to a CommanderScoreAverage");
		} catch (IllegalArgumentException e) {
			//as expected. Should not be able to add an Ammo score to a CommanderScoreAverage.
		}
	}
	
	@Test
	public void getAthletes() {
		final LeaguePositionScores scores = new LeaguePositionScores(LEAGUE_A_COMMANDER);
		Assert.assertEquals(Collections.EMPTY_SET, scores.getAthletes());

		// Add first score
		scores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {1000}));
		Assert.assertEquals(PLAYER_A_ONLY, scores.getAthletes());

		// Add second score
		scores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {2000}));
		Assert.assertEquals(PLAYER_A_ONLY, scores.getAthletes());
		
		// Add third score - extra PLayer.
		scores.addScores(playerScoresFactory.create(PLAYER_B, LEAGUE_A_COMMANDER, new Integer[] {3000}));
		Assert.assertEquals(PLAYER_A_AND_B, scores.getAthletes());
	}
}
