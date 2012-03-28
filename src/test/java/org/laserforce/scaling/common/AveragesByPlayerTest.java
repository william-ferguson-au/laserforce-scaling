package org.laserforce.scaling.common;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Responsible for testing the LeaguePositionScores.
 * 
 * @author williamf
 */
public final class AveragesByPlayerTest {

    private final PlayerScoresFactory playerScoresFactory = new PlayerScoresFactory();

	private final League LEAGUE = new League("A");
	
	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE, GamePosition.COMMANDER);
	private final LeaguePosition LEAGUE_A_HEAVY = new LeaguePosition(LEAGUE, GamePosition.HEAVY);
	private final LeaguePosition LEAGUE_A_SCOUT = new LeaguePosition(LEAGUE, GamePosition.SCOUT);
	private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(LEAGUE, GamePosition.AMMO);
	
	private final Athlete PLAYER_A = new Athlete("Tragedy");

    @Test
	public void addAverage() {
		final ScoresByPlayer scores = new ScoresByPlayer();
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_HEAVY, new Integer[] {12000, 13000, 11000, 12200, 11800}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_SCOUT, new Integer[] {8000, 7000, 6000, 7500}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {6000, 4000, 5500}));

		final ScoresForPlayer scoresPlayerA = scores.getScoresFor(PLAYER_A);
		Assert.assertEquals(4, scoresPlayerA.getScoresForAllPositions().size());
	}
}
