package org.laserforce.scaling.ratio;

import org.junit.Assert;
import org.junit.Test;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.AveragesForPlayer;
import org.laserforce.scaling.common.LeagueLevel;
import org.laserforce.scaling.common.PlayerDetail;

import java.util.ArrayList;
import java.util.List;

public final class RankerTest {

    private final Athlete RUSTY = new Athlete("Rusty");
    private final Athlete TRAGEDY = new Athlete("Tragedy");
    private final Athlete MEOW = new Athlete("Meow");

    private final PlayerRanker ranker = new PlayerRanker(1, 12);

	@Test
	public void rankEmptyCollection() {
		final List<LeagueLevel> playerRank = ranker.rank(new ArrayList<PlayerDetail>());
		Assert.assertEquals(0, playerRank.size());
	}
	
    @Test
    /**
     * Show what would happen if no scores be scaled.
     */
    public void rankCollectionOfPlayerDetailsWithOnlyOneAverage() {

        final List<PlayerDetail> playerDetails = new ArrayList<PlayerDetail>();
        playerDetails.add(new PlayerDetail(new AveragesForPlayer(RUSTY), new AveragesForPlayer(RUSTY), 6000, 13, 10));

        final List<LeagueLevel> leagueLevels = ranker.rank(playerDetails);
        for (final LeagueLevel leagueLevel : leagueLevels) {
            if (leagueLevel.getLevel() == 12) {
                Assert.assertEquals("Should have exactly one player", 1, leagueLevel.getNrPlayers());
            } else {
                Assert.assertEquals("Should have no players", 0, leagueLevel.getNrPlayers());
            }
        }
        Assert.assertEquals(12, leagueLevels.size());
    }


    @Test
    /**
     * Show what would happen if no scores be scaled.
     */
    public void rankCollectionOfPlayerDetailsWithTwoAverages() {

        final List<PlayerDetail> playerDetails = new ArrayList<PlayerDetail>();
        playerDetails.add(new PlayerDetail(new AveragesForPlayer(RUSTY), new AveragesForPlayer(RUSTY), 6000, 13, 10));
        playerDetails.add(new PlayerDetail(new AveragesForPlayer(TRAGEDY), new AveragesForPlayer(TRAGEDY), 5000, 10, 10));

        final List<LeagueLevel> leagueLevels = ranker.rank(playerDetails);
        for (final LeagueLevel leagueLevel : leagueLevels) {
            if ((leagueLevel.getLevel() == 12) || (leagueLevel.getLevel() == 1)) {
                Assert.assertEquals("Should have exactly one player", 1, leagueLevel.getNrPlayers());
            } else {
                Assert.assertEquals("Should have no players", 0, leagueLevel.getNrPlayers());
            }
        }
        Assert.assertEquals(12, leagueLevels.size());
    }

    @Test
    /**
     * Show what would happen if no scores be scaled.
     */
    public void rankCollectionOfPlayerDetailsWithInvalidDetail() {

        final List<PlayerDetail> playerDetails = new ArrayList<PlayerDetail>();
        playerDetails.add(new PlayerDetail(new AveragesForPlayer(RUSTY), new AveragesForPlayer(RUSTY), 6000, 13, 10));
        playerDetails.add(new PlayerDetail(new AveragesForPlayer(MEOW), new AveragesForPlayer(MEOW), 0, 30, 0));

        try {
            ranker.rank(playerDetails);
            Assert.fail("Should not be able to rank Players when they do not all have transformed scores.");
        } catch (IllegalArgumentException e) {
            // as expected;
        }
    }


}
