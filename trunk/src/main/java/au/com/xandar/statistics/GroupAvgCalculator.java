package au.com.xandar.statistics;

import java.util.Collection;

/**
 * Calculates a GroupAvg for a Collection of objects.
 *
 * @author williamf
 */
public interface GroupAvgCalculator<T> {

    // TODO Consider that this function is really an instance of a generic Collection functor. Ie CollectionResult #processCollection(Collection)

	/**
	 * @param values    Collection of values for which to calculate the GroupStat.
	 * @return GroupAverage of the provided values.
	 */
	public GroupAvg getAverageFor(Collection<T> values);
}
