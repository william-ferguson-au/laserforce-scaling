package org.laserforce.scaling.common;

import au.com.xandar.collection.SimpleList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

/**
 * Responsible for capturing the result of a scaling.
 * <p/>
 * User: William
 * Date: 08/06/2010
 * Time: 7:30:03 PM
 */
public final class ScaledResult {

    private final int minimumNrGames;
    private final int minimumLevel;
    private final int maximumLevel;
    private final LeaguePosition targetPosition;
    private final List<LeagueLevel> leagueLevels;
    private final Collection<PlayerDetail> untransformedPlayers;
    private final SortedSet<LeaguePosition> leaguePositions;
    private SimpleList<FileDefinition> fileDefinitions = new SimpleList<FileDefinition>();

    public ScaledResult(int minimumNrGames, int minimumLevel, int maximumLevel, LeaguePosition targetPosition, List<LeagueLevel> leagueLevels, Collection<PlayerDetail> untransformedPlayers, SortedSet<LeaguePosition> leaguePositions) {
        this.minimumNrGames = minimumNrGames;
        this.minimumLevel = minimumLevel;
        this.maximumLevel = maximumLevel;
        this.targetPosition = targetPosition;
        this.leagueLevels = Collections.unmodifiableList(leagueLevels);
        this.untransformedPlayers = Collections.unmodifiableCollection(untransformedPlayers);
        this.leaguePositions = Collections.unmodifiableSortedSet(leaguePositions);
    }

    public int getMinimumNrGames() {
        return minimumNrGames;
    }

    public int getMinimumLevel() {
        return minimumLevel;
    }

    public int getMaximumLevel() {
        return maximumLevel;
    }

    public LeaguePosition getTargetPosition() {
        return targetPosition;
    }

    public List<LeagueLevel> getLeagueLevels() {
        return leagueLevels;
    }

    public boolean hasUntransformedPlayers() {
        return !untransformedPlayers.isEmpty();
    }

    public Collection<PlayerDetail> getUntransformedPlayers() {
        return untransformedPlayers;
    }

    public SortedSet<LeaguePosition> getLeaguePositions() {
        return leaguePositions;
    }

    public SimpleList<FileDefinition> getFileDefinitions() {
        return fileDefinitions;
    }

    public void setFileDefinitions(SimpleList<FileDefinition> fileDefinitions) {
        this.fileDefinitions = fileDefinitions;
    }
}
