package org.laserforce.scaling.common;

import java.util.Arrays;

/**
 * Responsible for creating instances of PlayerAverage.
 * <p/>
 * User: William
 * Date: 02/05/2010
 * Time: 11:49:58 PM
 */
public final class PlayerScoresFactory {

    public PlayerScores create(Athlete athlete, LeaguePosition leaguePosition, Integer[] scores) {
        return new PlayerScores(athlete, leaguePosition, Arrays.asList(scores));
    }
}
