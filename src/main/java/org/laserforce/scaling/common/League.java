package org.laserforce.scaling.common;

/**
 * Represents a particular Laserforce league.
 * 
 * @author williamf
 */
public final class League implements Comparable<League> {

	private final String name;

	public League(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(League o) {
		return name.compareTo(o.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof League)) return false;
		final League other = (League) obj;
		return name.equals(other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}
