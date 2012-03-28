package org.laserforce.scaling.ratio;

import org.laserforce.scaling.common.LeagueLevel;
import org.laserforce.scaling.common.PlayerDetail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Responsible for ranking a Collection of Athletes based upon their average.
 * <p>
 * This involves ordering the Athletes from lowest to highest and determining a rank (level) for each. 
 * </p>
 * 
 * @author williamf
 */
public final class PlayerRanker {

	private final int bottomLevel;
	private final int topLevel;
	
	public PlayerRanker(int bottomLevel, int topLevel) {
		this.bottomLevel = bottomLevel;
		this.topLevel = topLevel;
	}

    public int getBottomLevel() {
        return bottomLevel;
    }

    public int getTopLevel() {
        return topLevel;
    }

    /**
	 * Takes a Collection of PlayerDetail and groups them into levels from highest to lowest.
	 *
	 * @param playerDetails	Collection of PlayerDetails to rank from highest to lowest.
	 * @return List of LeagueLevel ordered from highest to lowest showing minimum and maximum for each level and containing those PlayerDetail within that LeagueLevel.
     * @throws IllegalArgumentException if one of the supplied PlayerDetail contains no transformed scores. 
	 */
	public List<LeagueLevel> rank(Collection<PlayerDetail> playerDetails) {
		
		if (playerDetails.isEmpty()) {
			return new ArrayList<LeagueLevel>();
		}
		
		// Order the players from highest to lowest
		final LinkedList<PlayerDetail> sortedPlayerDetails = new LinkedList<PlayerDetail>();
		sortedPlayerDetails.addAll(playerDetails);
		Collections.sort(sortedPlayerDetails);
		
        final double topAvg = sortedPlayerDetails.getFirst().getAverage();
		final double bottomAvg = sortedPlayerDetails.getLast().getAverage();
		final int nrLevels = topLevel - bottomLevel + 1;
		final double levelWidth = (topAvg - bottomAvg) / nrLevels;
		
		// Add the first LevelMinimum and set the bar for the next one.
        final List<LeagueLevel> leagueLevels = new ArrayList<LeagueLevel>();
        for (int i = topLevel ; i >= bottomLevel; i--) {
            final double levelMinimum = bottomAvg + ((i - bottomLevel) * levelWidth);
            final double levelMaximimum = levelMinimum + levelWidth;
            leagueLevels.add(new LeagueLevel(i, levelMinimum, levelMaximimum));
        }

        final Iterator<LeagueLevel> iter = leagueLevels.iterator();
        LeagueLevel currentLeagueLevel = iter.next();
        for (final PlayerDetail playerDetail : sortedPlayerDetails) {

            if (playerDetail.getNrGamesTransformed() == 0) {
                throw new IllegalArgumentException("Cannot rank players when some do not have transformed scores : " + playerDetail);
            }

            while (playerDetail.getAverage() < currentLeagueLevel.getLevelMinimum()) {
                currentLeagueLevel = iter.next();
            }
            currentLeagueLevel.addPlayerDetail(playerDetail);
        }

		return leagueLevels;
	}
}
