package org.laserforce.scaling.ratio;

import org.laserforce.scaling.common.PlayerDetail;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the transformation of scores for an entire League of Players.
 * <p/>
 * User: William
 * Date: 11/06/2010
 * Time: 3:00:02 AM
 */
final class TransformationResult {

    private final Collection<PlayerDetail> transformedPlayers = new ArrayList<PlayerDetail>();
    private final Collection<PlayerDetail> untransformedPlayers = new ArrayList<PlayerDetail>();

    public void addTransformedPlayer(PlayerDetail player) {
        transformedPlayers.add(player);
    }

    public void addUntransformedPlayer(PlayerDetail player) {
        untransformedPlayers.add(player);
    }

    public Collection<PlayerDetail> getTransformedPlayers() {
        return transformedPlayers;
    }

    public Collection<PlayerDetail> getUntransformedPlayers() {
        return untransformedPlayers;
    }
}
