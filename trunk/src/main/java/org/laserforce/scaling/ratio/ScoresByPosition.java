package org.laserforce.scaling.ratio;

import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScores;
import org.laserforce.scaling.common.ScoresByPlayer;
import org.laserforce.scaling.common.ScoresForPlayer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for collating the averages per LeaguePosition.
 * 
 * @author williamf
 */
final class ScoresByPosition {

	private final Map<LeaguePosition, LeaguePositionScores> averages = new HashMap<LeaguePosition, LeaguePositionScores>();
	
	public void addScores(PlayerScores playerScores) {
		
		if (!averages.containsKey(playerScores.getLeaguePosition())) { // Add a new LeaguePositionScores if one does not already exist.
			averages.put(playerScores.getLeaguePosition(), new LeaguePositionScores(playerScores.getLeaguePosition()));
		}
		
		final LeaguePositionScores leagueAvg = averages.get(playerScores.getLeaguePosition());
		leagueAvg.addScores(playerScores);
	}
	
    public Collection<LeaguePosition> getLeaguePositions() {
        return averages.keySet();
    }

    public LeaguePositionScores getLeaguePositionScores(LeaguePosition leaguePosition) {
        return averages.get(leaguePosition);
    }

	@Override
	public String toString() {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(stringWriter);
		
		writer.println("ScoresByPosition [");
		for (final LeaguePositionScores leaguePositionAverages: averages.values()) {
			writer.println("  Position : " + leaguePositionAverages.getLeaguePosition());
			for (final PlayerScores scores : leaguePositionAverages.getAllScores()) {
				writer.println("    " + scores);
			}
		}
		
		writer.print("]");
		
		return stringWriter.toString();
	}
	
	
	/**
	 * Converts ScoresByPlayer into ScoresByPosition.
	 * <p>
	 * That is, it converts a Collection of PlayerAverages grouped by Athlete into a Collection of PlayerAverage group by LeaguePosition.
	 * </p>
	 * <p>
	 * Note that it only includes those PlayerAverages where the player has played at least the minimum number of games supplied.
	 * </p>
	 * 
	 * @param scoresByPlayer	ScoresByPlayer to transform into AvergesByPosition.
	 * @param minNrGames		Minimum number of games that an Athlete must have played in a position 
	 * 							for those scores to be included in the new ScoresByPosition.
	 * @return AveragesByPlayer transformed into ScoresByPosition.
	 */
	public static ScoresByPosition createFrom(ScoresByPlayer scoresByPlayer, int minNrGames) {
		
		final ScoresByPosition scoresByPosition = new ScoresByPosition();
		for (final Athlete athlete : scoresByPlayer.getAthletes()) {
			final ScoresForPlayer scoresForPlayer = scoresByPlayer.getScoresFor(athlete);
			for (final PlayerScores playerScores : scoresForPlayer.getScoresForAllPositions()) {
				if (playerScores.getNrGames() >= minNrGames) {
					scoresByPosition.addScores(playerScores);
				}
			}
		}
		
		return scoresByPosition;
	}

}
