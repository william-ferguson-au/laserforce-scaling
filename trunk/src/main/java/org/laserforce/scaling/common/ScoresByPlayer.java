package org.laserforce.scaling.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsible for collating averages across many LeaguePositions for many Athletes.
 *  
 * @author williamf
 */
public final class ScoresByPlayer {

	private final Map<Athlete, ScoresForPlayer> scoresForAllPlayers = new HashMap<Athlete, ScoresForPlayer>();
    private final SortedSet<LeaguePosition> leaguePositions = new TreeSet<LeaguePosition>(); // SortedSet so that we return the LeaguePositions in their natural order.

    public void addPlayerScores(PlayerScores average) {
		if (!scoresForAllPlayers.containsKey(average.getAthlete())) {
			scoresForAllPlayers.put(average.getAthlete(), new ScoresForPlayer(average.getAthlete()));
		}
		
		final ScoresForPlayer averagesForPlayer = scoresForAllPlayers.get(average.getAthlete());
		averagesForPlayer.addScores(average);
        leaguePositions.add(average.getLeaguePosition());
	}

    /**
     * Add all the averages from the supplied ScoresByPlayer to this ScoresByPlayer.
     *
     * @param otherScores ScoresByPlayer to add to this ScoresByPlayer.
     */
    public void addAverages(ScoresByPlayer otherScores) {
        for (final ScoresForPlayer averagesForPlayer : otherScores.scoresForAllPlayers.values()) {
            for (final PlayerScores playerScores : averagesForPlayer.getScoresForAllPositions()) {
                addPlayerScores(playerScores);
            }
        }
    }

    /**
     * @return Set of Athletes that have scores collated by this ScoresByPlayer ordered by Athlete.
     */
	public Set<Athlete> getAthletes() {
        final SortedSet<Athlete> athletes = new TreeSet<Athlete>();
		athletes.addAll(scoresForAllPlayers.keySet());
        return athletes;
	}

    /**
     * @return Set of the LeaguePosition (in natural order) for all the PlayerAverage added to this ScoresByPlayer.
     */
    public SortedSet<LeaguePosition> getLeaguePositions() {
        return Collections.unmodifiableSortedSet(leaguePositions);
    }

	public ScoresForPlayer getScoresFor(Athlete player) {
		return scoresForAllPlayers.get(player);
	}
	
    public boolean hasScoresFor(Athlete player, LeaguePosition leaguePosition) {
        if (!scoresForAllPlayers.containsKey(player)) {
            return false;
        }
        final ScoresForPlayer averagesForPlayer = scoresForAllPlayers.get(player);
        return averagesForPlayer.getScoresForPosition(leaguePosition) != null;
    }

	@Override
	public String toString() {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(stringWriter);
		
		writer.println("ScoresByPlayer [");
		for (final Athlete athlete : scoresForAllPlayers.keySet()) {
			writer.println("  Athlete: " + athlete);
			final ScoresForPlayer scoresForPlayer = scoresForAllPlayers.get(athlete);
			for (final PlayerScores playerPositionScores : scoresForPlayer.getScoresForAllPositions()) {
				writer.println("    " + playerPositionScores);
			}
		}
		
		writer.print("]");
		
		return stringWriter.toString();
	}
}
