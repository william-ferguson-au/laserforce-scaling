package org.laserforce.scaling.ratio;

import org.apache.log4j.Logger;
import org.laserforce.scaling.common.LeagueLevel;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.ScaledResult;
import org.laserforce.scaling.common.ScoresByPlayer;

import java.util.List;

/**
 * Responsible for scaling a collection of Athletes.
 * <p>
 * It does so by aggregating the average scores across all positions played by an athlete over a season.
 * The scores are aggregated (across positions) using the average and standard deviation of the ratio between two positions for all players.
 * </p>
 * <p>
 * Once they have been aggregated (for an athlete), they are then translated into a nominated position.
 * Ie the athletes are given a an aggregated score (across all positions), represented as as a score in one nominated position. 
 * </p>
 * <p>
 * Players are then ranked from highest to lowest based upon that aggregated score 
 * and a ranking level is determined for each player for purposes of the league level cap. 
 * </p>
 *
 * @author williamf
 */
public final class Scaler {

	private static final Logger LOGGER = Logger.getLogger(Scaler.class);

    private PlayerRanker ranker = new PlayerRanker(1, 12);
    private int minNrGamesToConsider = 3;

    /**
     * Default PlayerRanker ranks linearly between levels 1 and 12.
     *
     * @param ranker    PlayerRanker to use to rank the scaled scores.
     */
    public void setRanker(PlayerRanker ranker) {
        this.ranker = ranker;
    }

    /**
     * Default minimum number of games to consider is 3.
     *
     * @param minNrGamesToConsider  Minimum number of games to include when performing the scaling.
     */
    public void setMinNrGamesToConsider(int minNrGamesToConsider) {
        this.minNrGamesToConsider = minNrGamesToConsider;
    }

    /**
     * Scales the averages to the target position.
     *
     * @param scoresByPlayer  All the scores grouped by player. They are used to determine the transformation ratios as well as provide input for the transformation.
     * @param targetPosition    LeaguePosition to which all scores will be scaled.
     * @return ScaledResult of the scores scaled to a single target Position.
     */
    public ScaledResult scale(ScoresByPlayer scoresByPlayer, LeaguePosition targetPosition) {

        LOGGER.debug("scoresByPlayer: " + scoresByPlayer);

        // Calculate ratios for each pair of LeaguePosition.
        final RatioCalculator calculator = new RatioCalculator(minNrGamesToConsider);
        final RatiosBetweenPosition ratios = calculator.calculateRatios(scoresByPlayer);
        LOGGER.debug("ratiosBetweenPositions : " + ratios);

        // Transform all player scores to the target position.
        final ScoreTransformer transformer = new ScoreTransformer(ratios, minNrGamesToConsider);
        final TransformationResult transformationResult = transformer.transformAveragesForAllPlayers(scoresByPlayer, targetPosition);
        LOGGER.debug("allTransformedAveragesTransformedToASinglePosition: " + transformationResult.getTransformedPlayers());

        // Rank the ScalingResults
        final List<LeagueLevel> leagueLevels = ranker.rank(transformationResult.getTransformedPlayers());

        return new ScaledResult(minNrGamesToConsider, ranker.getBottomLevel(), ranker.getTopLevel(), targetPosition, leagueLevels, transformationResult.getUntransformedPlayers(), scoresByPlayer.getLeaguePositions());
    }
}
