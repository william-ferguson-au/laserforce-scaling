package org.laserforce.scaling.io;

import org.laserforce.scaling.common.FileDefinition;
import org.laserforce.scaling.common.LeagueLevel;
import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerAverage;
import org.laserforce.scaling.common.PlayerDetail;
import org.laserforce.scaling.common.ScaledResult;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.SortedSet;

/**
 * Responsible for printing Levels in a clear and concise format.
 * <p/>
 * User: William
 * Date: 24/04/2010
 * Time: 5:41:25 PM
 */
public final class ScaledResultPrinter {

    private final PrintStream stream;
    private boolean printScaledPositionScores;

    public ScaledResultPrinter(PrintStream stream) {
        this.stream = stream;
    }

    /**
     * @param printScaledPositionScores    Whether to also print the scaled scores for each individual position.
     */
    public void setPrintScaledPositionScores(boolean printScaledPositionScores) {
        this.printScaledPositionScores = printScaledPositionScores;
    }

    private static final int ATHLETE_COLUMN_WIDTH = 22;
    private static final int OVERALL_SCORE_COLUMN_WIDTH = 13; 
    private static final int POSITION_SCORE_COLUMN_WIDTH = 13; 
    
    private static final String AVG_GAMES_HEADER = "Avg NrGames";

    public void print(ScaledResult scaledResult) {

        stream.println();

        // Print MinNrGames selected, etc
        stream.println("Minimum number of games required in a League Position : " + scaledResult.getMinimumNrGames());

        // Print Min/Max Levels
        stream.printf("Ranked between levels %d and %d", scaledResult.getMinimumLevel(), scaledResult.getMaximumLevel());
        stream.println();

        // Print targetPosition,
        stream.println("Overall score for each player scaled to : " + scaledResult.getTargetPosition());

        // Print Files loaded,
        if (!scaledResult.getFileDefinitions().isEmpty()) {
            stream.println();
            stream.println("Loaded scores from:");
            for (final FileDefinition fileDefinition : scaledResult.getFileDefinitions()) {
                stream.printf("    %-20s  %-40s", fileDefinition.getLeaguePosition(), fileDefinition.getFile());
                stream.println();
            }
        }

        // print header
        stream.println();
        printHeading(scaledResult.getLeaguePositions());

        // print each level and its contained PlayerDetails
        for (final LeagueLevel leagueLevel : scaledResult.getLeagueLevels()) {
            stream.println();
            printLevelHeading(leagueLevel, scaledResult.getLeaguePositions());
            for (final PlayerDetail playerDetail : leagueLevel.getPlayerDetails()) {
            	printOverallScoreAndOriginalsScores(playerDetail, scaledResult.getLeaguePositions());
                if (printScaledPositionScores) {
                    printTransformedPositionScores(playerDetail, scaledResult.getLeaguePositions());
                }
            }
        }

        // List those players that haven't been scaled because they don't have any scores that qualify.
        if (scaledResult.hasUntransformedPlayers()) {
            stream.println();
            printUntransformedPlayersHeading(scaledResult.getLeaguePositions());
            for (final PlayerDetail playerDetail : scaledResult.getUntransformedPlayers()) {
            	printOverallScoreAndOriginalsScores(playerDetail, scaledResult.getLeaguePositions());
            }
        }

        stream.println();
        stream.println("NB bracketed scores indicate a score that has been disregarded for scaling purposes.");
        stream.println("   Probably because it is less than the minimum number of games for a League Position.");
    }

    private void printHeading(SortedSet<LeaguePosition> leaguePositions) {
        stream.printf("%-" + ATHLETE_COLUMN_WIDTH + "s", "Player");
        stream.printf("%" + OVERALL_SCORE_COLUMN_WIDTH + "s", AVG_GAMES_HEADER);
        for (int i = 0; i < leaguePositions.size(); i++) {
            stream.printf("%" + POSITION_SCORE_COLUMN_WIDTH + "s", AVG_GAMES_HEADER);
        }
        stream.println();
    }
    
    // NB printf("%-s", foo) means left justify foo, otherwise foo will be right justified.
    
    private void printLevelHeading(LeagueLevel leagueLevel, SortedSet<LeaguePosition> leaguePositions) {
        final String levelString = printToString("Level %2d  (%5.0f -%5.0f) Scaled-Avg", leagueLevel.getLevel(), leagueLevel.getLevelMinimum(), leagueLevel.getLevelMaximum());
        stream.printf("%-" + (ATHLETE_COLUMN_WIDTH + OVERALL_SCORE_COLUMN_WIDTH) + "s ", levelString);
        for (final LeaguePosition leaguePosition : leaguePositions) {
            final String positionDescription = printToString("%2s-%-10s", leaguePosition.getLeague().getName(), leaguePosition.getPosition());
            stream.printf("%" + POSITION_SCORE_COLUMN_WIDTH + "s", positionDescription);
        }
        stream.println();
    }
    
    private void printUntransformedPlayersHeading(SortedSet<LeaguePosition> leaguePositions) {
        stream.printf("%-" + (ATHLETE_COLUMN_WIDTH + OVERALL_SCORE_COLUMN_WIDTH) + "s ", "Untransformed Players");
        for (final LeaguePosition leaguePosition : leaguePositions) {
            final String positionDescription = printToString("%2s-%-10s", leaguePosition.getLeague().getName(), leaguePosition.getPosition());
            stream.printf("%" + POSITION_SCORE_COLUMN_WIDTH + "s", positionDescription);
        }
        stream.println();
    }

    private void printOverallScoreAndOriginalsScores(PlayerDetail playerDetail, SortedSet<LeaguePosition> leaguePositions) {
        stream.printf("%-" + ATHLETE_COLUMN_WIDTH + "s", playerDetail.getAthleteName());
        
        final String overallScoreDetails = printToString("%5.0f  %3d", playerDetail.getAverage(), playerDetail.getNrGamesPlayed());
        stream.printf("%" + OVERALL_SCORE_COLUMN_WIDTH + "s", overallScoreDetails);

        for (final LeaguePosition leaguePosition : leaguePositions) {
            final PlayerAverage average = playerDetail.getOriginalAverageFor(leaguePosition);
            final String positionScoreDetails;
            if (average != null) {
                // Print original scores that weren't used in brackets.
                final boolean noTransformedAverage = playerDetail.hasNoTransformedAverageFor(leaguePosition);
                final String leftBracket = noTransformedAverage ? "(" : "";
                final String rightBracket = noTransformedAverage ? ")" : "";
                
                positionScoreDetails = printToString("%1s%5.0f %2d%1s  ", leftBracket, average.getAverageScore(), average.getNrGames(), rightBracket);
            } else {
                positionScoreDetails = "";
            }
            stream.printf("%" + POSITION_SCORE_COLUMN_WIDTH + "s", positionScoreDetails);
        }
        stream.println();
    }
    
    /**
     * Iterate over all LeaguePositions and print the transformed  AvgScore and NrGames (probably on the next line).
     * 
     * @param playerDetail		Details of the player whose transformed scores should be printed.
     * @param leaguePositions	LeaguePositions that a player may have played. 
     */
    private void printTransformedPositionScores(PlayerDetail playerDetail, SortedSet<LeaguePosition> leaguePositions) {
        stream.printf("%" + ATHLETE_COLUMN_WIDTH + "s", "scaled to ..");

        stream.printf("%" + OVERALL_SCORE_COLUMN_WIDTH + "s", "");

        for (final LeaguePosition leaguePosition : leaguePositions) {
            final PlayerAverage average = playerDetail.getTransformedAverageFor(leaguePosition);
            final String positionScoreDetails;
            if (average != null) {
                positionScoreDetails = printToString("%5.0f %2d   ", average.getAverageScore(), average.getNrGames());
            } else {
                positionScoreDetails = "";
            }
            stream.printf("%" + POSITION_SCORE_COLUMN_WIDTH + "s", positionScoreDetails);
        }
        stream.println();
    }
    
    private String printToString(String format, Object... args) {
    	final OutputStream stream = new ByteArrayOutputStream();
    	final PrintStream printStream = new PrintStream(stream);
    	printStream.printf(format, args);
    	return stream.toString();
    }
}
