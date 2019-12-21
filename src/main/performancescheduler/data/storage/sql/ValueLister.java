package performancescheduler.data.storage.sql;

import java.util.List;

interface ValueLister<T> {
	/**
	 * Get the complete list of column names in the order their values are presented by this class.
	 * @return ordered list of column names
	 */
    public List<String> columnOrder();
	/**
	 * Builds a comma-separated, ()-bound list of the subject's values in the appropriate column order.
	 * @param object whose values will be listed
	 * @return new String containing those values
	 */
    public String listValues(T subject);
}