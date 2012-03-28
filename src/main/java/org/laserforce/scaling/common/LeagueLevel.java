package org.laserforce.scaling.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a LeagueLevel and all the Players that fall within that level.
 * <p>
 * It can also be used to represent minimum values for a level.
 * </p>
 *  
 * @author williamf
 */
public final class LeagueLevel {

    private final int level;
    private final double levelMinimum;
    private final double levelMaximum;
    private final List<PlayerDetail> playerDetails = new ArrayList<PlayerDetail>();

    public LeagueLevel(int level, double levelMinimum, double levelMaximum) {
        this.level = level;
        this.levelMinimum = levelMinimum;
        this.levelMaximum = levelMaximum;
    }

    public void addPlayerDetail(PlayerDetail detail) {
        playerDetails.add(detail);
    }

    public List<PlayerDetail> getPlayerDetails() {
        return playerDetails;
    }

    public int getNrPlayers() {
        return playerDetails.size();
    }

    public int getLevel() {
        return level;
    }

    public double getLevelMinimum() {
        return levelMinimum;
    }

    public double getLevelMaximum() {
        return levelMaximum;
    }

    @Override
    public String toString() {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter writer = new PrintWriter(stringWriter);

        writer.print("LeagueLevel [level=");
        writer.print(level);
        writer.print(", levelMin=");
        writer.print(levelMinimum);
        writer.print(", levelMax=");
        writer.print(levelMaximum);
        writer.print(", Players={");

        boolean firstPlayer = true;
        for (final PlayerDetail playerDetail: playerDetails) {
            if (firstPlayer) {
                writer.println(); // Start all players on a new line.
                firstPlayer = false;
            }
            writer.println("  " + playerDetail);
        }

        writer.print("}]");

        return stringWriter.toString();
    }
}
