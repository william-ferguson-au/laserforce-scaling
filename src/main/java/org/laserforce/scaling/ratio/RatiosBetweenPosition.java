package org.laserforce.scaling.ratio;

import org.laserforce.scaling.common.LeaguePosition;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for maintaining the ratios of the averages between various LeaguePositions.  
 * 
 * @author williamf
 */
final class RatiosBetweenPosition {

	/**
	 * Provides a handy Collection of each of the targetPositions for each fromPosition.
	 */
	private final Map<LeaguePosition, Set<LeaguePositionRatio>> ratiosFromPosition = new HashMap<LeaguePosition, Set<LeaguePositionRatio>>();
	private final Map<LeaguePositionTuple, LeaguePositionRatio> ratios = new HashMap<LeaguePositionTuple, LeaguePositionRatio>();

	/**
	 * Pair of Positions, used to define the relationship of a LeaguePositionRatio.
	 * 
	 * @author williamf
	 */
	private static final class LeaguePositionTuple {

		private final LeaguePosition from;
		private final LeaguePosition to;
		
		public LeaguePositionTuple(LeaguePosition from, LeaguePosition to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public int hashCode() {
			return from.hashCode() + to.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof LeaguePositionTuple)) return false;
			final LeaguePositionTuple other = (LeaguePositionTuple) obj;
			return other.from.equals(from) && other.to.equals(to);
		}

		@Override
		public String toString() {
			return "LeaguePositions [" + "from=" + from + ", " + "to=" + to + "]";
		}
	}

    /**
     * @param ratio LeaguePositionRatio to add to this Collection of ratios between Positions.
     */
	public void add(LeaguePositionRatio ratio) {
		ratios.put(new LeaguePositionTuple(ratio.getFromPosition(), ratio.getTargetPosition()), ratio);
		
		// Track the toPositions for each fromPosition. 
		if (!ratiosFromPosition.containsKey(ratio.getFromPosition())) {
			ratiosFromPosition.put(ratio.getFromPosition(), new HashSet<LeaguePositionRatio>());
		}
		final Set<LeaguePositionRatio> toPositions = ratiosFromPosition.get(ratio.getFromPosition());
		toPositions.add(ratio);
	}

    /**
     * @param fromPosition  LeaguePosition from which valid ratios are to be returned.
     * @return Collection<LeaguePositionRatio> of those ratios that exists from the supplied LeaguePosition.
     */
    public Collection<LeaguePositionRatio> getRatiosFrom(LeaguePosition fromPosition) {
        return ratiosFromPosition.get(fromPosition);
    }

	public LeaguePositionRatio getRatioBetween(LeaguePosition from, LeaguePosition to) {
		return ratios.get(new LeaguePositionTuple(from, to));
	}

	@Override
	public String toString() {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(stringWriter);

		writer.println("RatiosBetweenPosition [");

		final List<LeaguePositionRatio> orderedRatios = new ArrayList<LeaguePositionRatio>();
		orderedRatios.addAll(ratios.values());
		Collections.sort(orderedRatios);

		for (final LeaguePositionRatio ratio : orderedRatios) {
			writer.println("  " + ratio);
		}

		writer.print("]");

		return stringWriter.toString();
	}
}
