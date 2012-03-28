package org.laserforce.scaling.io;

import org.junit.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.GamePosition;
import org.laserforce.scaling.common.League;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScores;
import org.laserforce.scaling.common.ScoresByPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Responsible for testing the CsvScoreReader.
 * <p/>
 * User: William
 * Date: 01/05/2010
 * Time: 6:39:38 PM
 */
public final class CsvScoreReaderTest {

    private final LeaguePosition LEAGUE_A_AMMO = new LeaguePosition(new League("A"), GamePosition.AMMO);
    private final ScoreReader reader = new CsvScoreReader("", 0, 5);
    private final File testFile = new File(this.getClass().getResource("score-reader-test-a.csv").getFile());

    private final Athlete RUSTY = new Athlete("Rusty");
    private final Athlete ZIM = new Athlete("Zim");

    @Test
    public void readFile() throws IOException {

        final ScoresByPlayer scoresByPlayer = reader.read(testFile, LEAGUE_A_AMMO);
        Assert.assertEquals(2, scoresByPlayer.getAthletes().size());

        final PlayerScores zimAverage = scoresByPlayer.getScoresFor(ZIM).getScoresForPosition(LEAGUE_A_AMMO);
        Assert.assertArrayEquals(new Integer[] {3222}, zimAverage.getScores().toArray());
        Assert.assertEquals(1, zimAverage.getNrGames());

        final PlayerScores rustyAverage = scoresByPlayer.getScoresFor(RUSTY).getScoresForPosition(LEAGUE_A_AMMO);
        Assert.assertArrayEquals(new Integer[] {8002,6522,5202,5962,6842,7402,4361,6721}, rustyAverage.getScores().toArray());
        Assert.assertEquals(8, rustyAverage.getNrGames());
    }
}
