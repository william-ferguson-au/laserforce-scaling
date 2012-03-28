package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupStat;
import au.com.xandar.statistics.GroupStatCalculator;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerScores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Responsible for collating the all scores for a league game position.  
 * 
 * @author williamf
 */
final class LeaguePositionScores {

	private final LeaguePosition leaguePosition;
	private final List<PlayerScores> playerScoreCollection = new ArrayList<PlayerScores>();

	// Calculate the StdDev from each score.
    private final GroupStatCalculator<PlayerScores> calculator = new PlayerScoresGroupStatCalculator();

    public LeaguePositionScores(LeaguePosition position) {
		this.leaguePosition = position;
	}

    public LeaguePosition getLeaguePosition() {
		return leaguePosition;
	}

	/**
	 * Adds a playerScore to this LeaguePositionScores.
	 * 
	 * @param score PlayerScores to aggregate for this LeaguePosition.
	 * @throws IllegalArgumentException if the score's position does not match that for this average. 
	 */
	public void addScores(PlayerScores score) {
		if (!score.getLeaguePosition().equals(leaguePosition)) {
			throw new IllegalArgumentException("Score <" + score + "> is not for same position as Average : " + this.leaguePosition);
		}
		playerScoreCollection.add(score);
	}

	/**
	 * @return Collection of Athlete that have scores for this LeaguePosition.
	 */
	public Set<Athlete> getAthletes() {
		final Set<Athlete> athletes = new HashSet<Athlete>();
		for (final PlayerScores score : playerScoreCollection) {
			athletes.add(score.getAthlete());
		}
		return athletes;
	}
	
    public Collection<PlayerScores> getAllScores() {
        return playerScoreCollection;
    }
    
	/**
	 * @param athletes	Set of Athletes for which to calculate group average and standard deviation.
	 * @return GroupStatistic containing the Average, StdDev and NrGames for games played by the supplied Athletes in this LeaguePosition.
	 */
	public GroupStat getAverageFor(Set<Athlete> athletes) {
        final Collection<PlayerScores> scoresForMatchingAthletes = new ArrayList<PlayerScores>();
        for (final PlayerScores playerScores : playerScoreCollection) {
            if (athletes.contains(playerScores.getAthlete())) {
                scoresForMatchingAthletes.add(playerScores); // Only include scores for players that ARE part of the requested set.
            }
        }

        // Calculate the Average, StdDev and NrGames for games played by the supplied Athletes in this LeaguePosition.
		return calculator.getAverageFor(scoresForMatchingAthletes);
	}

    @Override
    public String toString() {
        return "LeaguePositionScores{" + leaguePosition +
                ", scores=" + playerScoreCollection +
                '}';
    }
}
