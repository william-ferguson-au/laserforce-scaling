package org.laserforce.scaling.ratio;

import junit.framework.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerAverage;
import org.laserforce.scaling.common.PlayerDetail;
import org.laserforce.scaling.common.PlayerScoresFactory;
import org.laserforce.scaling.common.ScoresByPlayer;
import org.laserforce.scaling.common.ScoresForPlayer;

import java.util.Collection;

/**
 * Responsible for testing the LeaguePositionScores.
 * 
 * @author williamf
 */
public final class ScoreTransformerTest {

    private final PlayerScoresFactory playerScoresFactory = new PlayerScoresFactory();

	private final League LEAGUE = new League("A");
	
	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE, GamePosition.COMMANDER);
	private final LeaguePosition LEAGUE_A_SCOUT = new LeaguePosition(LEAGUE, GamePosition.SCOUT);
	private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(LEAGUE, GamePosition.AMMO);
	
	private final Athlete PLAYER_A = new Athlete("Tragedy");
	
	private final RatiosBetweenPosition ratios = new RatiosBetweenPosition();
    private final ScoreTransformer transformer = new ScoreTransformer(ratios, 3);

    public ScoreTransformerTest() {
		ratios.add(new LeaguePositionRatio(LEAGUE_A_COMMANDER, LEAGUE_A_COMMANDER, 10500, 10500, 2100, 2100, 13));
		ratios.add(new LeaguePositionRatio(LEAGUE_A_COMMANDER, LEAGUE_A_SCOUT, 10500, 6800, 2100, 3200, 8));
		ratios.add(new LeaguePositionRatio(LEAGUE_A_SCOUT, LEAGUE_A_SCOUT, 6800, 6800, 3200, 3200, 15));
		ratios.add(new LeaguePositionRatio(LEAGUE_A_AMMO, LEAGUE_A_SCOUT, 5200, 6800, 1800, 3200, 12));
		ratios.add(new LeaguePositionRatio(LEAGUE_A_AMMO, LEAGUE_A_AMMO, 5200, 5200, 2100, 2100, 18));
	}


    @Test
	public void transformAveragesForAllPlayers() {

		final ScoresByPlayer scoresByPlayer = new ScoresByPlayer();
		scoresByPlayer.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));

		final TransformationResult transformationResult = transformer.transformAveragesForAllPlayers(scoresByPlayer, LEAGUE_A_SCOUT);
        final Collection<PlayerDetail> playerDetails = transformationResult.getTransformedPlayers();

        Assert.assertEquals(1, playerDetails.size());

        final PlayerDetail playerDetail = playerDetails.iterator().next();
		Assert.assertEquals(PLAYER_A, playerDetail.getAthlete());
		Assert.assertEquals(7, playerDetail.getNrGamesPlayed());
		Assert.assertEquals(9085, playerDetail.getAverage(), 1.0);
	}

    @Test
	public void transformAveragesToTargetPosition() {
		
		final ScoresForPlayer scores = new ScoresForPlayer(PLAYER_A);
		scores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));
		
		final PlayerDetail playerDetail = transformer.transformAveragesToTargetPosition(scores, LEAGUE_A_SCOUT);
		
		Assert.assertEquals(PLAYER_A, playerDetail.getAthlete());
		Assert.assertEquals(7, playerDetail.getNrGamesPlayed());
		Assert.assertEquals(9085, playerDetail.getAverage(), 1.0);
	}
	
	@Test
	public void getTransformedValue() {
		final PlayerAverage scoreToTransform = new PlayerAverage(PLAYER_A, LEAGUE_A_COMMANDER, 9000, 4);
		final PlayerAverage transformedScore = transformer.getTransformedValue(scoreToTransform, LEAGUE_A_SCOUT);

		Assert.assertEquals(PLAYER_A, transformedScore.getAthlete());
		Assert.assertEquals(4, transformedScore.getNrGames());
		Assert.assertEquals(4514, transformedScore.getAverageScore(), 1.0);
	}
}
