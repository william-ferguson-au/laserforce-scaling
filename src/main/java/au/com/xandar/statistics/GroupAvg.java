package au.com.xandar.statistics;

public final class GroupAvg {

	private final int weighting;
	private final double average;

	public GroupAvg(double average, int weighting) {
		this.weighting = weighting;
		this.average = average;
	}

    public int getWeighting() {
		return weighting;
	}

	public double getAverage() {
		return average;
	}

	@Override
	public String toString() {
		return "GroupAvg [average=" + average + ", weighting=" + weighting + "]";
	}
}