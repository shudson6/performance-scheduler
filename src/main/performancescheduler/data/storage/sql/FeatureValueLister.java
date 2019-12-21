package performancescheduler.data.storage.sql;

import java.util.Arrays;
import java.util.List;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaWrapper;

class FeatureValueLister extends ValueLister<MetaFeature> {
	
	@Override
	public List<String> columnOrder() {
		return Arrays.asList(COL_ORDER);
	}
	
	String colValue(String col, MetaFeature ftr) {
		switch (col) {
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
			default:
				return super.colValue(col, ftr);
		}
	}
	
	private final String[] COL_ORDER = new String[] {
		SQL.COL_UUID, SQL.COL_TITLE, SQL.COL_RATING, SQL.COL_RUNTIME, SQL.COL_IS3D, SQL.COL_CC, SQL.COL_OC, SQL.COL_DA,
		SQL.COL_CREATED, SQL.COL_CHANGED, SQL.COL_ACTIVE
	};
}
