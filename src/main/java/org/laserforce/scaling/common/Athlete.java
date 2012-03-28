package org.laserforce.scaling.common;

/**
 * Represents a competitor in a Laserforce competition. 
 * 
 * @author williamf
 */
public final class Athlete implements Comparable<Athlete> {

	private final String codeName;
	
	public Athlete(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeName() {
		return codeName;
	}

	
	@Override
	public int hashCode() {
		return codeName.hashCode();
	}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Athlete)) return false;
        final Athlete other = (Athlete) obj;
        return codeName.equals(other.codeName);
    }

    @Override
    public int compareTo(Athlete o) {
        return codeName.compareTo(o.codeName);
    }

    @Override
	public String toString() {
		return codeName;
	}
}
