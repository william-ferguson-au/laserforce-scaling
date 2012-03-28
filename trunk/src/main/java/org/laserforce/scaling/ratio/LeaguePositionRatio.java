package org.laserforce.scaling.ratio;

import org.laserforce.scaling.common.LeaguePosition;
import org.laserforce.scaling.common.PlayerAverage;

/**
 * Responsible for tabulating and calculating the ratio between 2 LeagueGamePositions.
 * 
 * @author williamf
 */
final class LeaguePositionRatio implements Comparable<LeaguePositionRatio> {

	private final LeaguePosition fromPosition; // Required so that we can validate that a transform is occurring from the correct position.
	private final LeaguePosition targetPosition; // Required so that we know to which position this ratio transforms an average.
	private final double fromPositionAvg;
	private final double targetPositionAvg;
	private final double fromPositionStdDev;
	private final double targetPositionStdDev;
	private final int nrGames;

    public LeaguePositionRatio(LeaguePosition fromPosition, LeaguePosition targetPosition, double fromPositionAvg, double targetPositionAvg, double position1StdDev, double position2StdDev, int nrGames) {
		this.fromPosition = fromPosition;
		this.targetPosition = targetPosition;
		this.fromPositionAvg = fromPositionAvg;
		this.targetPositionAvg = targetPositionAvg;
		this.fromPositionStdDev = position1StdDev;
		this.targetPositionStdDev = position2StdDev;
		this.nrGames = nrGames;
	}
    
	public LeaguePosition getFromPosition() {
		return fromPosition;
	}

	public LeaguePosition getTargetPosition() {
		return targetPosition;
	}

	public double getFromPositionAvg() {
		return fromPositionAvg;
	}

	public double getTargetPositionAvg() {
		return targetPositionAvg;
	}

	public double getFromPositionStdDev() {
		return fromPositionStdDev;
	}

	public double getTargetPositionStdDev() {
		return targetPositionStdDev;
	}

	public int getNrGames() {
		return nrGames;
	}

	/**
	 * A PlayerAverage may not be able to be transformed because its fromPosition does not match the fromPosition
	 * for this LeaguePositionRatio, or because this LeaguePositionRatio does not contain enough information to perform a valid transformation.  
	 * 
	 * @param originalScore	PlayerAverage to transform.
	 * @return true if this ratio is capable of transforming the supplied score.
	 */
	public boolean canTransform(PlayerAverage originalScore) {
		
		if (fromPositionDoesNotMatch(originalScore.getLeaguePosition())) {
			return false;
		}
		
		if (nrGames == 0) {
			return false; // Cannot transform if the ratio is based upon zero games (ie it has no weight).
		}
		
		return !(fromPositionStdDevIsZero() || targetPositionStdDevIsZero());  
	}

	/**
	 * Transforms a PlayerAverage from the fromPosition to the targetPosition.
     * <p>
     * Before calling this method, a client should first call {@link #canTransform(PlayerAverage)} to determine
     * whether a transformation can be made for a PlayerAverage.
     * </p>
	 * 
	 * @param originalScore	PlayerAverage to transform from one LeaguePosition to another.
	 * @return PlayerAverage that has has been transformed from one LeaguePosition to another. 
	 * @throws IllegalArgumentException if the PLayerAverage cannot be transformed.
	 */
	public PlayerAverage transform(PlayerAverage originalScore) {

        if (fromPositionDoesNotMatch(originalScore.getLeaguePosition())) {
            throw new IllegalArgumentException("Cannot transform score with fromPosition " + originalScore.getLeaguePosition() + " with this ratio : " + this);
        }
        if (nrGames == 0) {
            throw new IllegalArgumentException("Cannot transform score when the ratio is based on a weighting of zero games : " + this);
        }
        if (fromPositionStdDevIsZero()) {
            throw new IllegalArgumentException("Cannot transform score when ratio's fromPosition StdDev is zero : " + this);
        }
        if (targetPositionStdDevIsZero()) {
            throw new IllegalArgumentException("Cannot transform score when ratio's targetPosition StdDev is zero : " + this);
        }

		final double avgScore = targetPositionAvg + (originalScore.getAverageScore() - fromPositionAvg) / getRatioOfStdDev();
		final int transformedGames = Math.min(nrGames, originalScore.getNrGames()); 
		
		return new PlayerAverage(originalScore.getAthlete(), targetPosition, avgScore, transformedGames);
	}


    private boolean fromPositionDoesNotMatch(LeaguePosition otherFromPosition) {
        return !fromPosition.equals(otherFromPosition);
    }

    private boolean fromPositionStdDevIsZero() {
        return fromPositionStdDev == 0;
    }

    private boolean targetPositionStdDevIsZero() {
        return targetPositionStdDev == 0;
    }

	private double getRatioOfStdDev() {
		return fromPositionStdDev / targetPositionStdDev;
	}

	@Override
	public int compareTo(LeaguePositionRatio o) {
		final int fromCompare = fromPosition.compareTo(o.fromPosition);
		if (fromCompare != 0) {
			return fromCompare;
		}
		return targetPosition.compareTo(o.targetPosition);
	}
	
	@Override
	public int hashCode() {
		return fromPosition.hashCode() + targetPosition.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LeaguePositionRatio)) {
			return false;
		}
		final LeaguePositionRatio other = (LeaguePositionRatio) obj;
		if (!fromPosition.equals(other.fromPosition)) return false;
		return targetPosition.equals(other.targetPosition);
	}

	@Override
	public String toString() {
		return "LeaguePositionRatio [" +
				"from=[" + fromPosition.getLeague() + ", " + fromPosition.getPosition() + "], " + 
				"to=[" + targetPosition.getLeague() + ", " + targetPosition.getPosition() + "], " + 
				"nrGames=" + nrGames + 
				", fromAvg=" + fromPositionAvg + ", targetAvg=" + targetPositionAvg +
				", fromStdDev=" + fromPositionStdDev + ", targetStdDev=" + targetPositionStdDev + "]";
	}
}
