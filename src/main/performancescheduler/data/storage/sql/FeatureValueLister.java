package performancescheduler.data.storage.sql;

import java.util.Arrays;
import java.util.List;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaWrapper;

class FeatureValueLister implements ValueLister<MetaFeature> {
	public final int ENTRY_SIZE = 141;
	
	@Override
	public List<String> columnOrder() {
		return Arrays.asList(COL_ORDER);
	}
	
	@Override
	public String listValues(MetaFeature ftr) {
		StringBuilder sb = new StringBuilder(ENTRY_SIZE);
		if (ftr != null && !ftr.getTitle().equals(MetaWrapper.NULLSTR)) {
		    sb.append("(");
    		for (String s : COL_ORDER) {
    			sb.append(colValue(s, ftr) + ",");
    		}
    		sb.deleteCharAt(sb.lastIndexOf(","));
    		sb.append(")");
		}
		return sb.toString();
	}
	
	String colValue(String col, MetaFeature ftr) {
		switch (col) {
			case SQL.COL_UUID:
				return quotes(ftr.getUuid().toString());
			case SQL.COL_TITLE:
				return quotes(ftr.getTitle());
			case SQL.COL_RATING:
				return quotes(ftr.getRating().toString());
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
				return quotes(ftr.getCreatedTimestamp().format(SQL.DATETIME_FMT));
			case SQL.COL_CHANGED:
				return quotes(ftr.getChangedTimestamp().format(SQL.DATETIME_FMT));
			case SQL.COL_ACTIVE:
				return "true";
			default:
				return "[null]";
		}
	}
	
	private String quotes(String str) {
	    return "'" + str + "'";
	}
	
	private final String[] COL_ORDER = new String[] {
		SQL.COL_UUID, SQL.COL_TITLE, SQL.COL_RATING, SQL.COL_RUNTIME, SQL.COL_IS3D, SQL.COL_CC, SQL.COL_OC, SQL.COL_DA,
		SQL.COL_CREATED, SQL.COL_CHANGED, SQL.COL_ACTIVE
	};
}
