package performancescheduler.data.storage.sql;

import java.time.format.DateTimeFormatter;

public class SQL {
	public static final String TBL_FEATURE = "featuredata";
	public static final String COL_UUID = "uuid";
	public static final String COL_TITLE = "title";
	public static final String COL_RATING = "rating";
	public static final String COL_RUNTIME = "runtime";
	public static final String COL_IS3D = "is3d";
	public static final String COL_CC = "cc";
	public static final String COL_OC = "oc";
	public static final String COL_DA = "da";
	public static final String COL_CREATED = "created";
	public static final String COL_CHANGED = "changed";
	public static final String COL_ACTIVE = "active";
	
	public static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private SQL() {}
}
