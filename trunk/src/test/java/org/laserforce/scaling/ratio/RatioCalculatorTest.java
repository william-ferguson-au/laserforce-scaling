package org.laserforce.scaling.ratio;

import org.junit.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScoresFactory;
import org.laserforce.scaling.common.ScoresByPlayer;

public final class RatioCalculatorTest {

    private final PlayerScoresFactory playerScoresFactory = new PlayerScoresFactory();

	private final League LEAGUE_A = new League("A");

    private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE_A, GamePosition.COMMANDER);
	private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(LEAGUE_A, GamePosition.AMMO);
	private final LeaguePosition LEAGUE_A_MEDIC = new LeaguePosition(LEAGUE_A, GamePosition.MEDIC);
	
    private final Athlete PLAYER_A = new Athlete("Tragedy");
    private final Athlete PLAYER_B = new Athlete("WarDog");
    private final Athlete PLAYER_C = new Athlete("Foo");
    
    private static final double DOUBLE_VARIANCE = 1.0;

    @Test
	public void calculateRatios_Empty_FromAmmoToMedic() {
		final ScoresByPlayer scores = new ScoresByPlayer();
		
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_B, LEAGUE_A_AMMO, new Integer[] {12000, 13000, 11000, 12200}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_B, LEAGUE_A_MEDIC, new Integer[] {2000, 3000}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_C, LEAGUE_A_MEDIC, new Integer[] {1000, 1300, 1100}));

		final RatioCalculator calculator = new RatioCalculator(3); // Will exclude WarDog's medic score, so there will be no commonality between Ammo and Medic.
		final RatiosBetweenPosition ratios = calculator.calculateRatios(scores);
		
		Assert.assertEquals(2, ratios.getRatiosFrom(LEAGUE_A_AMMO).size());
		Assert.assertEquals(2, ratios.getRatiosFrom(LEAGUE_A_MEDIC).size());
		
		// No scores in common, so ratio should be zero.
		final LeaguePositionRatio a2mRatio = ratios.getRatioBetween(LEAGUE_A_AMMO, LEAGUE_A_MEDIC);
		Assert.assertEquals(0, a2mRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(0, a2mRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(0, a2mRatio.getNrGames());
		Assert.assertEquals(0, a2mRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
		Assert.assertEquals(0, a2mRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
		
		// No scores in common, so ratio should be zero.
		final LeaguePositionRatio m2aRatio = ratios.getRatioBetween(LEAGUE_A_AMMO, LEAGUE_A_MEDIC);
		Assert.assertEquals(0, m2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(0, m2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(0, m2aRatio.getNrGames());
		Assert.assertEquals(0, m2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
		Assert.assertEquals(0, m2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
	}
	
    @Test
	public void calculateRatios_Simple_FromAmmoToMedic() {
		final ScoresByPlayer scores = new ScoresByPlayer();
		
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {5500, 5000, 6200, 6200}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_MEDIC, new Integer[] {2000, 2400}));
		
		final RatioCalculator calculator = new RatioCalculator(2);
		final RatiosBetweenPosition ratios = calculator.calculateRatios(scores);
		
		Assert.assertEquals(2, ratios.getRatiosFrom(LEAGUE_A_AMMO).size());
		Assert.assertEquals(2, ratios.getRatiosFrom(LEAGUE_A_MEDIC).size());
		
		final LeaguePositionRatio a2aRatio = ratios.getRatioBetween(LEAGUE_A_AMMO, LEAGUE_A_AMMO);
		Assert.assertEquals(5725, a2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(5725, a2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(4, a2aRatio.getNrGames());
		Assert.assertEquals(506, a2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
		Assert.assertEquals(506, a2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
		
		final LeaguePositionRatio a2mRatio = ratios.getRatioBetween(LEAGUE_A_AMMO, LEAGUE_A_MEDIC);
		Assert.assertEquals(5725, a2mRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(2200, a2mRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(2, a2mRatio.getNrGames());
		Assert.assertEquals(506, a2mRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
		Assert.assertEquals(200, a2mRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
		
		final LeaguePositionRatio m2aRatio = ratios.getRatioBetween(LEAGUE_A_MEDIC, LEAGUE_A_AMMO);
		Assert.assertEquals(2200, m2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(5725, m2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(2, m2aRatio.getNrGames());
		Assert.assertEquals(200, m2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
		Assert.assertEquals(506, m2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
		
		final LeaguePositionRatio m2mRatio = ratios.getRatioBetween(LEAGUE_A_MEDIC, LEAGUE_A_MEDIC);
		Assert.assertEquals(2200, m2mRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(2200, m2mRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
		Assert.assertEquals(2, m2mRatio.getNrGames());
		Assert.assertEquals(200, m2mRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
		Assert.assertEquals(200, m2mRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
	}

    @Test
    public void calculateRatioBetween_OnePlayerInCommon() {

        final LeaguePositionScores commanderScores = new LeaguePositionScores(LEAGUE_A_COMMANDER);
        commanderScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));

        final LeaguePositionScores ammoScores = new LeaguePositionScores(LEAGUE_A_AMMO);
        ammoScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {6000,7000, 5000, 6000}));

        final RatioCalculator calculator = new RatioCalculator(3);
        final LeaguePositionRatio c2aRatio = calculator.calculateRatioBetween(commanderScores, ammoScores);

        Assert.assertEquals(LEAGUE_A_COMMANDER, c2aRatio.getFromPosition());
        Assert.assertEquals(LEAGUE_A_AMMO, c2aRatio.getTargetPosition());
        Assert.assertEquals(12000, c2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(1200, c2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(6000, c2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(707, c2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(4, c2aRatio.getNrGames());
    }

    @Test
    public void calculateRatioBetween_NoPlayerInCommon() {

        final LeaguePositionScores commanderScores = new LeaguePositionScores(LEAGUE_A_COMMANDER);
        commanderScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));

        final LeaguePositionScores ammoScores = new LeaguePositionScores(LEAGUE_A_AMMO);
        ammoScores.addScores(playerScoresFactory.create(PLAYER_B, LEAGUE_A_AMMO, new Integer[] {6000,7000, 5000, 6000}));

        final RatioCalculator calculator = new RatioCalculator(3);
        final LeaguePositionRatio c2aRatio = calculator.calculateRatioBetween(commanderScores, ammoScores);

        Assert.assertEquals(LEAGUE_A_COMMANDER, c2aRatio.getFromPosition());
        Assert.assertEquals(LEAGUE_A_AMMO, c2aRatio.getTargetPosition());
        Assert.assertEquals(0, c2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(0, c2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(0, c2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(0, c2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(0, c2aRatio.getNrGames());
    }

    @Test
    public void calculateRatioBetween_StdDevOfZeroInFromPosition() {

        final LeaguePositionScores commanderScores = new LeaguePositionScores(LEAGUE_A_COMMANDER);
        commanderScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 12000, 12000, 12000, 12000}));

        final LeaguePositionScores ammoScores = new LeaguePositionScores(LEAGUE_A_AMMO);
        ammoScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {6000,7000, 5000, 6000}));

        final RatioCalculator calculator = new RatioCalculator(3);
        final LeaguePositionRatio c2aRatio = calculator.calculateRatioBetween(commanderScores, ammoScores);

        Assert.assertEquals(LEAGUE_A_COMMANDER, c2aRatio.getFromPosition());
        Assert.assertEquals(LEAGUE_A_AMMO, c2aRatio.getTargetPosition());
        Assert.assertEquals(12000, c2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(0, c2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(6000, c2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(707, c2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(4, c2aRatio.getNrGames());
    }


    @Test
    public void calculateRatioBetween_StdDevOfZeroInTargetPosition() {

        final LeaguePositionScores commanderScores = new LeaguePositionScores(LEAGUE_A_COMMANDER);
        commanderScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));

        final LeaguePositionScores ammoScores = new LeaguePositionScores(LEAGUE_A_AMMO);
        ammoScores.addScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_AMMO, new Integer[] {6000,6000, 6000, 6000}));

        final RatioCalculator calculator = new RatioCalculator(3);
        final LeaguePositionRatio c2aRatio = calculator.calculateRatioBetween(commanderScores, ammoScores);

        Assert.assertEquals(LEAGUE_A_COMMANDER, c2aRatio.getFromPosition());
        Assert.assertEquals(LEAGUE_A_AMMO, c2aRatio.getTargetPosition());
        Assert.assertEquals(12000, c2aRatio.getFromPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(1200, c2aRatio.getFromPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(6000, c2aRatio.getTargetPositionAvg(), DOUBLE_VARIANCE);
        Assert.assertEquals(0, c2aRatio.getTargetPositionStdDev(), DOUBLE_VARIANCE);
        Assert.assertEquals(4, c2aRatio.getNrGames());
    }

}
