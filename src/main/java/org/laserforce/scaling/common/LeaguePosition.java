package org.laserforce.scaling.common;

/**
 * Represents a position played within a particular league.
 * <p>
 * This abstraction is necessary when attempting to athletes from two ore more leagues
 * together where there is some cross over of athletes between the leagues. 
 * </p>
 * 
 * @author williamf
 */
public final class LeaguePosition implements Comparable<LeaguePosition> {

	private final League league;
	private final GamePosition position;
	
	public LeaguePosition(League league, GamePosition position) {
		this.league = league;
		this.position = position;
	}

	public League getLeague() {
		return league;
	}

	public GamePosition getPosition() {
		return position;
	}

	@Override
	public int compareTo(LeaguePosition o) {
		final int comparePosition = position.compareTo(o.position);
		if (comparePosition != 0) {
			return comparePosition;
		}
		return league.compareTo(o.league);
	}

	@Override
	public int hashCode() {
		return league.hashCode() + position.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LeaguePosition)) return false;
		final LeaguePosition other = (LeaguePosition) obj;
		return league.equals(other.league) && position.equals(other.position);
	}

	@Override
	public String toString() {
		return league.getName() + "-" + position.getName();
	}
}
