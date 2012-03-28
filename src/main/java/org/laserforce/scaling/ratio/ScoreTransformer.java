package org.laserforce.scaling.ratio;

import au.com.xandar.statistics.GroupAvg;
import au.com.xandar.statistics.GroupAvgCalculator;
import au.com.xandar.statistics.IntegerGroupAvgCalculator;
import org.apache.log4j.Logger;
import org.laserforce.scaling.common.Athlete;
import org.laserforce.scaling.common.AveragesForPlayer;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerAverage;
import org.laserforce.scaling.common.PlayerDetail;
import org.laserforce.scaling.common.PlayerScores;
import org.laserforce.scaling.common.ScoresByPlayer;
import org.laserforce.scaling.common.ScoresForPlayer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Responsible for transforming a PlayerAverage into an average for a target position.
 * <p>
 * This is achieved by transforming the original average to all other league positions
 * and then transforming from those intermediate averages to the target position.
 * </p>
 * <p>
 * The result is the weighted average of all values transformed to the target position.
 * </p>
 *
 * User: William
 * Date: 26/04/2010
 * Time: 2:32:31 PM
 */
final class ScoreTransformer {

    private static final Logger LOGGER = Logger.getLogger(ScoreTransformer.class);

    private final GroupAvgCalculator<Integer> playerScoresGroupAvgCalculator = new IntegerGroupAvgCalculator();
    private final GroupAvgCalculator<PlayerAverage> playerAverageGroupAvgCalculator = new PlayerAverageGroupAvgCalculator();
    private final RatiosBetweenPosition ratios;
    private final int minNrGamesToConsider;

    public ScoreTransformer(RatiosBetweenPosition ratios, int minNrGamesToConsider) {
        this.ratios = ratios;
        this.minNrGamesToConsider = minNrGamesToConsider;
    }

    /**
     * Transform the averages for all players to the target position.
     *
     * @param scoresByPlayer  ScoresByPlayer for all player to transform.
     * @param targetPosition    LeaguePosition to which to transform the averages.
     * @return TransformationResult of all the transformed and untransformed averages.
     */
    public TransformationResult transformAveragesForAllPlayers(ScoresByPlayer scoresByPlayer, LeaguePosition targetPosition) {

        final TransformationResult transformationResult = new TransformationResult();

        for (final Athlete athlete : scoresByPlayer.getAthletes()) {
            final ScoresForPlayer scoresForPlayer = scoresByPlayer.getScoresFor(athlete);
            final PlayerDetail playerDetail = transformAveragesToTargetPosition(scoresForPlayer, targetPosition);
            if (playerDetail.getNrGamesTransformed() > 0) {
                transformationResult.addTransformedPlayer(playerDetail);
            } else {
                transformationResult.addUntransformedPlayer(playerDetail);
            }
        }
        
        return transformationResult;
    }

    /**
     * Transform scores for the given athlete to the target position.
     * <p>
     * This is achieved by first transforming the averages to every position for which a ratio exists,
     * and then transforming those averages to the target position. Performing this double translation
     * ensures that the scores are evenly weighted for all positions and that any ordering (or ranking)
     * of the resultant averages does not change based upon the target position.
     * </p>
     *
     * @param scoresForPlayer		Scores for each position for a player.
     * @param targetPosition 		LeaguePosition to which to transform all the scores.
     * @return PlayerDetails containing an average which is an amalgam of the values for each position transformed to the target position.
     *      PlayerDetails will only contain games for which some amount of transformation was possible.
     */
    public PlayerDetail transformAveragesToTargetPosition(ScoresForPlayer scoresForPlayer, LeaguePosition targetPosition) {

        final AveragesForPlayer originalAverages = new AveragesForPlayer(scoresForPlayer.getAthlete());
        final AveragesForPlayer transformedAverages = new AveragesForPlayer(scoresForPlayer.getAthlete());

        int nrGamesPlayed = 0;
        int nrGamesTransformed = 0;

        // Iterate over all the scores, and transform each one to the target position via all possible intermediate positions.
        for (final PlayerScores scoresToTransform : scoresForPlayer.getScoresForAllPositions()) {

            // Transform PlayerScores to PlayerAverage at this point (Average and NrGames is not required beforehand)
            final GroupAvg scoresGroupAvg = playerScoresGroupAvgCalculator.getAverageFor(scoresToTransform.getScores());
            final PlayerAverage scoreToTransform =  new PlayerAverage(scoresToTransform.getAthlete(), scoresToTransform.getLeaguePosition(), scoresGroupAvg.getAverage(), scoresGroupAvg.getWeighting());
            originalAverages.addAverage(scoreToTransform);

            if (scoreToTransform.getNrGames() < minNrGamesToConsider) {
                continue; // Too few games to consider.
            }
            LOGGER.debug("Transforming " + scoreToTransform + " to final target " + targetPosition);

            final PlayerAverage transformedAverage = getTransformedValue(scoreToTransform, targetPosition);
            if (transformedAverage != null) {
                transformedAverages.addAverage(transformedAverage);
                nrGamesTransformed += scoreToTransform.getNrGames();
            }
            nrGamesPlayed += scoreToTransform.getNrGames();
        }

        final GroupAvg groupAvg = playerAverageGroupAvgCalculator.getAverageFor(transformedAverages.getAverages());
        LOGGER.debug("Transformed Average: " + groupAvg);

        return new PlayerDetail(originalAverages, transformedAverages, groupAvg.getAverage(), nrGamesPlayed, nrGamesTransformed);
    }

    /**
     * Iterate over all the positions with ratios from scoreToTransform position,
     * transform the supplied score to that intermediate position and then transform the intermediateScore to the target position.
     *
     * @param scoreToTransform  Score to transform to the target position.
     * @param targetPosition    Position to which to transform the supplied score.
     * @return PlayerAverage for fromPosition which is the weighted average of the transformed scores, or null if no transformations were possible.
     */
    public PlayerAverage getTransformedValue(PlayerAverage scoreToTransform, LeaguePosition targetPosition) {

        final Collection<PlayerAverage> transformedScores = new ArrayList<PlayerAverage>();
        final LeaguePosition fromPosition = scoreToTransform.getLeaguePosition();

        // Get the targetPositions for which we have a ratio for that fromPosition.
        for (final LeaguePositionRatio originalToIntermediateRatio : ratios.getRatiosFrom(fromPosition)) {
            final LeaguePosition intermediatePosition = originalToIntermediateRatio.getTargetPosition();
            if (!originalToIntermediateRatio.canTransform(scoreToTransform)) {
                LOGGER.debug("   !! Cannot perform intermediate transform " + fromPosition + " to " + intermediatePosition + " using : " + originalToIntermediateRatio);
            	continue; // Cannot transform if the ratio is not valid.
            }
            	
            final PlayerAverage intermediateScore = originalToIntermediateRatio.transform(scoreToTransform);

            LOGGER.debug("Transformed to intermediate " + intermediateScore);

            final LeaguePositionRatio intermediateToTargetRatio = ratios.getRatioBetween(intermediatePosition, targetPosition);
            if (intermediateToTargetRatio == null) {
                LOGGER.debug("   !! Cannot perform final transform " + intermediatePosition + " to " + targetPosition + "  -- No ratio!");
                continue; // No ratios from that Position.
            }
            if (!intermediateToTargetRatio.canTransform(intermediateScore)) {
                LOGGER.debug("   !! Cannot perform final transform " + intermediatePosition + " to " + targetPosition + " using : " + intermediateToTargetRatio);
                continue; // Ratio is not capable of performing the transform.
            }

            final PlayerAverage transformedScore = intermediateToTargetRatio.transform(intermediateScore);
            LOGGER.debug("            to final " + transformedScore);
            transformedScores.add(transformedScore);
        }

        if (transformedScores.isEmpty()) {
            return null;
        }
        
        final GroupAvg groupAvg = playerAverageGroupAvgCalculator.getAverageFor(transformedScores);
        
        return new PlayerAverage(scoreToTransform.getAthlete(), fromPosition, groupAvg.getAverage(), scoreToTransform.getNrGames());
    }
}
