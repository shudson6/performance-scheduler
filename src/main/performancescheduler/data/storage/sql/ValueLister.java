package performancescheduler.data.storage.sql;

import java.util.List;

import performancescheduler.data.storage.MetaWrapper;

abstract class ValueLister<T extends MetaWrapper<?>> {
	/**
	 * Get the complete list of column names in the order their values are presented by this class.
	 * @return ordered list of column names
	 */
    public abstract List<String> columnOrder();
	/**
	 * Builds a comma-separated, ()-bound list of the subject's values in the appropriate column order.
	 * @param object whose values will be listed
	 * @return new String containing those values
	 */
    public String listValues(T subject) {
		StringBuilder sb = new StringBuilder();
		if (subject != null && subject.getWrapped() != null) {
		    sb.append("(");
		    columnOrder().forEach(s -> sb.append(colValue(s, subject) + ","));
    		sb.deleteCharAt(sb.lastIndexOf(","));
    		sb.append(")");
		}
		return sb.toString();
	}
    
    String colValue(String col, T subject) {
        switch (col) {
			case SQL.COL_UUID:
				return quotes(subject.getUuid().toString());
			case SQL.COL_CREATED:
				return quotes(subject.getCreatedTimestamp().format(SQL.DATETIME_FMT));
			case SQL.COL_CHANGED:
				return quotes(subject.getChangedTimestamp().format(SQL.DATETIME_FMT));
			case SQL.COL_ACTIVE:
				return "true";
            default:
                return "[null]";
        }
    }
	
	protected final String quotes(String str) {
	    return "'" + str + "'";
	}
}