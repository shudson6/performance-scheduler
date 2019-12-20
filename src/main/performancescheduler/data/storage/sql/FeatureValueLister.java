package performancescheduler.data.storage.sql;

import java.util.Arrays;
import java.util.List;

import performancescheduler.data.storage.MetaFeature;

class FeatureValueLister {
	/**
	 * Get the complete list of column names in the order their values are presented by this class.
	 * @return ordered list of column names
	 */
	public List<String> columnOrder() {
		return Arrays.asList(COL_ORDER);
	}
	
	/**
	 * Builds a comma-separated, ()-bound list of the {@code MetaFeature}'s values in the appropriate column order.
	 * @param ftr the feature whose values are needed
	 * @return new string containing those values
	 */
	public String listValues(MetaFeature ftr) {
		StringBuilder sb = new StringBuilder(ENTRY_SIZE);
		sb.append("(");
		for (String s : COL_ORDER) {
			sb.append(colValue(s, ftr) + ",");
		}
		sb.replace(sb.lastIndexOf(","), sb.length(), ")");
		return sb.toString();
	}
	
	private String colValue(String col, MetaFeature ftr) {
		switch (col) {
			case SQL.COL_UUID:
				return ftr.getUuid().toString();
			case SQL.COL_TITLE:
				return ftr.getTitle();
			case SQL.COL_RATING:
				return ftr.getRating().toString();
			case SQL.COL_RUNTIME:
				return Integer.toString(ftr.getRuntime());
			case SQL.COL_IS3D:
				return Boolean.toString(ftr.is3d());
			case SQL.COL_CC:	
				return Boolean.toString(ftr.hasClosedCaptions());
			case SQL.COL_OC:
				return Boolean.toString(ftr.hasOpenCaptions());
			case SQL.COL_DA:
				return Boolean.toString(ftr.hasDescriptiveAudio());
			case SQL.COL_CREATED:
				return ftr.getCreatedTimestamp().toString();
			case SQL.COL_CHANGED:
				return ftr.getChangedTimestamp().toString();
			case SQL.COL_ACTIVE:
				return "true";
			default:
				return "[null]";
		}
	}
	
	public final int ENTRY_SIZE = 141;
	private final String[] COL_ORDER = new String[] {
		SQL.COL_UUID, SQL.COL_TITLE, SQL.COL_RATING, SQL.COL_RUNTIME, SQL.COL_IS3D, SQL.COL_CC, SQL.COL_OC, SQL.COL_DA,
		SQL.COL_CREATED, SQL.COL_CHANGED, SQL.COL_ACTIVE
	};
}
