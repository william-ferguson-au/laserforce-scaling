package au.com.xandar.statistics;

public final class GroupStat {

	private final int weighting;
	private final double average;
	private final double stdDeviation;

	public GroupStat(double average, double stdDeviation, int weighting) {
		this.weighting = weighting;
		this.average = average;
		this.stdDeviation = stdDeviation;
	}

    public int getWeighting() {
		return weighting;
	}

	public double getAverage() {
		return average;
	}

	public double getStdDeviation() {
		return stdDeviation;
	}
	
	@Override
	public String toString() {
		return "GroupStat [average=" + average + ", stdDeviation=" + stdDeviation + ", weighting=" + weighting + "]";
	}
}
