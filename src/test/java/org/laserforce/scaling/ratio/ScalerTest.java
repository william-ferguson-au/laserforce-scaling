package org.laserforce.scaling.ratio;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScoresFactory;
import org.laserforce.scaling.common.ScaledResult;
import org.laserforce.scaling.common.ScoresByPlayer;
import org.laserforce.scaling.io.CsvScoreReader;
import org.laserforce.scaling.io.ScaledResultPrinter;
import org.laserforce.scaling.io.ScoreReader;

import java.io.File;
import java.io.IOException;

/**
 * Responsible for testing the Scaler.
 * 
 * @author williamf
 */
public final class ScalerTest {

    private final PlayerScoresFactory playerScoresFactory = new PlayerScoresFactory();
    private final ScoreReader reader = new CsvScoreReader("", 0, 5);

	private final League LEAGUE_A = new League("A");
    private final League LEAGUE_C = new League("C");

	private final LeaguePosition LEAGUE_A_COMMANDER = new LeaguePosition(LEAGUE_A, GamePosition.COMMANDER);
	private final LeaguePosition LEAGUE_A_HEAVY = new LeaguePosition(LEAGUE_A, GamePosition.HEAVY);
	private final LeaguePosition LEAGUE_A_SCOUT = new LeaguePosition(LEAGUE_A, GamePosition.SCOUT);
	private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(LEAGUE_A, GamePosition.AMMO);
	private final LeaguePosition LEAGUE_A_MEDIC = new LeaguePosition(LEAGUE_A, GamePosition.MEDIC);
	
    private final LeaguePosition LEAGUE_C_COMMANDER = new LeaguePosition(LEAGUE_C, GamePosition.COMMANDER);
    private final LeaguePosition LEAGUE_C_HEAVY = new LeaguePosition(LEAGUE_C, GamePosition.HEAVY);
    private final LeaguePosition LEAGUE_C_SCOUT = new LeaguePosition(LEAGUE_C, GamePosition.SCOUT);
    private final LeaguePosition LEAGUE_C_AMMO = new LeaguePosition(LEAGUE_C, GamePosition.AMMO);
    private final LeaguePosition LEAGUE_C_MEDIC = new LeaguePosition(LEAGUE_C, GamePosition.MEDIC);

    private File A_COMMANDER_CSV;
    private File A_HEAVY_CSV;
    private File A_SCOUT_CSV;
    private File A_AMMO_CSV;
    private File A_MEDIC_CSV;

    private File C_COMMANDER_CSV;
    private File C_HEAVY_CSV;
    private File C_SCOUT_CSV;
    private File C_AMMO_CSV;
    private File C_MEDIC_CSV;

	private final Athlete PLAYER_A = new Athlete("Tragedy");
	private final Athlete PLAYER_B = new Athlete("WarDog");

    @Before
    public void populateFiles() {
        A_COMMANDER_CSV = new File(this.getClass().getResource("A-Grade-Sept-2009-Commander.csv").getFile());
        A_HEAVY_CSV = new File(this.getClass().getResource("A-Grade-Sept-2009-Heavy.csv").getFile());
        A_SCOUT_CSV = new File(this.getClass().getResource("A-Grade-Sept-2009-Scout.csv").getFile());
        A_AMMO_CSV = new File(this.getClass().getResource("A-Grade-Sept-2009-Ammo.csv").getFile());
        A_MEDIC_CSV = new File(this.getClass().getResource("A-Grade-Sept-2009-Medic.csv").getFile());

        C_COMMANDER_CSV = new File(this.getClass().getResource("C-Grade-Sept-2009-Commander.csv").getFile());
        C_HEAVY_CSV = new File(this.getClass().getResource("C-Grade-Sept-2009-Heavy.csv").getFile());
        C_SCOUT_CSV = new File(this.getClass().getResource("C-Grade-Sept-2009-Scout.csv").getFile());
        C_AMMO_CSV = new File(this.getClass().getResource("C-Grade-Sept-2009-Ammo.csv").getFile());
        C_MEDIC_CSV = new File(this.getClass().getResource("C-Grade-Sept-2009-Medic.csv").getFile());
    }

    @Test
	public void scale() {
		
        final int minLevel = 1;
        final int maxLevel = 3;
        final int nrLevels = maxLevel - minLevel + 1;

		final ScoresByPlayer scores = new ScoresByPlayer();
		
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_HEAVY, new Integer[] {9000, 8000, 8800}));

		scores.addPlayerScores(playerScoresFactory.create(PLAYER_B, LEAGUE_A_COMMANDER, new Integer[] {10000, 10500, 11000}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_B, LEAGUE_A_HEAVY, new Integer[] {7000, 7700, 7200}));

        final Scaler scaler = new Scaler();
        scaler.setRanker(new PlayerRanker(minLevel, maxLevel));
        final ScaledResult scaledResult = scaler.scale(scores, LEAGUE_A_HEAVY); // Scale to Position no-one has played.
        
		Assert.assertEquals("Must have same range of levels as ranked across", nrLevels, scaledResult.getLeagueLevels().size());
        Assert.assertEquals("Must have one Player in Min Level", 1, scaledResult.getLeagueLevels().get(0).getNrPlayers());
        Assert.assertEquals("Must have no Player in Level 2", 0, scaledResult.getLeagueLevels().get(1).getNrPlayers());
        Assert.assertEquals("Must have one Player in Max Level", 1, scaledResult.getLeagueLevels().get(2).getNrPlayers());
	}
	
	@Test
	public void scale_ImpossibleTarget() {
		
        final int minLevel = 1;
        final int maxLevel = 3;

		final ScoresByPlayer scores = new ScoresByPlayer();
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_COMMANDER, new Integer[] {12000, 13000, 11000, 12200, 11800, 10000, 14000}));
		scores.addPlayerScores(playerScoresFactory.create(PLAYER_A, LEAGUE_A_HEAVY, new Integer[] {11000, 9200, 10600}));

        final Scaler scaler = new Scaler();
        scaler.setRanker(new PlayerRanker(minLevel, maxLevel));
		final ScaledResult scaledResult = scaler.scale(scores, LEAGUE_A_SCOUT); // Scale to Position no-one has played.
        Assert.assertEquals(0, scaledResult.getLeagueLevels().size());
	}
	
	@Test
	public void scale_Mar_2010_A_and_C_grade() throws IOException {
		
        final int minLevel = 1;
        final int maxLevel = 12;
        final int nrLevels = maxLevel - minLevel + 1;

		final ScoresByPlayer scores = new ScoresByPlayer();
		
        addAGradeScores(scores);
        addCGradeScores(scores);

        final Scaler scaler = new Scaler();
        final ScaledResult scaledResult = scaler.scale(scores, LEAGUE_A_SCOUT); // Scale to Position no-one has played.
        Assert.assertEquals("Must have same number of Levels as requested", nrLevels, scaledResult.getLeagueLevels().size());

        final ScaledResultPrinter printer = new ScaledResultPrinter(System.out);
        printer.print(scaledResult);
	}

    @Test
    public void scale_Mar_2010_A_grade() throws IOException {

        final int minLevel = 2;
        final int maxLevel = 12;
        final int nrLevels = maxLevel - minLevel + 1;

        final ScoresByPlayer scores = new ScoresByPlayer();

        addAGradeScores(scores);

        final Scaler scaler = new Scaler();
        scaler.setRanker(new PlayerRanker(minLevel, maxLevel));
        final ScaledResult scaledResult = scaler.scale(scores, LEAGUE_A_SCOUT);
        Assert.assertEquals("Must have same number of Levels as requested", nrLevels, scaledResult.getLeagueLevels().size());

        final ScaledResultPrinter printer = new ScaledResultPrinter(System.out);
        printer.setPrintScaledPositionScores(true);
        printer.print(scaledResult);
    }

    @Test
    public void scale_Mar_2010_C_grade() throws IOException {

        final int minLevel = 1;
        final int maxLevel = 8;
        final int nrLevels = maxLevel - minLevel + 1;

        final ScoresByPlayer scores = new ScoresByPlayer();

        addCGradeScores(scores);

        final Scaler scaler = new Scaler();
        scaler.setRanker(new PlayerRanker(minLevel, maxLevel));
        final ScaledResult scaledResult = scaler.scale(scores, LEAGUE_C_SCOUT);
        Assert.assertEquals("Must have same number of Levels as requested", nrLevels, scaledResult.getLeagueLevels().size());

        final ScaledResultPrinter printer = new ScaledResultPrinter(System.out);
        printer.setPrintScaledPositionScores(true);
        printer.print(scaledResult);
    }

    private void addAGradeScores(ScoresByPlayer scores) throws IOException {
        scores.addAverages(reader.read(A_COMMANDER_CSV, LEAGUE_A_COMMANDER));
        scores.addAverages(reader.read(A_HEAVY_CSV, LEAGUE_A_HEAVY));
        scores.addAverages(reader.read(A_SCOUT_CSV, LEAGUE_A_SCOUT));
        scores.addAverages(reader.read(A_AMMO_CSV, LEAGUE_A_AMMO));
        scores.addAverages(reader.read(A_MEDIC_CSV, LEAGUE_A_MEDIC));
    }

    private void addCGradeScores(ScoresByPlayer scores) throws IOException {
        scores.addAverages(reader.read(C_COMMANDER_CSV, LEAGUE_C_COMMANDER));
        scores.addAverages(reader.read(C_HEAVY_CSV, LEAGUE_C_HEAVY));
        scores.addAverages(reader.read(C_SCOUT_CSV, LEAGUE_C_SCOUT));
        scores.addAverages(reader.read(C_AMMO_CSV, LEAGUE_C_AMMO));
        scores.addAverages(reader.read(C_MEDIC_CSV, LEAGUE_C_MEDIC));
    }
}
