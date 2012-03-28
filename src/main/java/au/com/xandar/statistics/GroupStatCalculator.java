package au.com.xandar.statistics;

import java.util.Collection;

/**
 * Calculates a GroupStat for a Collection of values.
 *
 * @author williamf
 */
public interface GroupStatCalculator<T> {

	/**
	 * @param values    Collection of value for which to calculate the GroupStat.
	 * @return GroupStatistic of the provided values.
	 */
	public GroupStat getAverageFor(Collection<T> values);
}