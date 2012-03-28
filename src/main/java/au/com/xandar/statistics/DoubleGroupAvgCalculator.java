package au.com.xandar.statistics;

import java.util.Collection;

/**
 * Calculates a GroupAvg for a Collection of Double.
 *
 * @author williamf
 */
public final class DoubleGroupAvgCalculator implements GroupAvgCalculator<Double> {

	/**
	 * @param values	Collection of Double for which to calculate the GroupAvg.
	 * @return GroupAverage of the provided values.
	 */
	public GroupAvg getAverageFor(Collection<Double> values) {
		
		double total = 0;
		for (final Double value : values) {
			total += value;
		}
		
		final double average = values.isEmpty() ? 0 : total / values.size();
		
		return new GroupAvg(average, values.size());
	}
	
}
