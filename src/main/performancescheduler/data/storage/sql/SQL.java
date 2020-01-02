package performancescheduler.data.storage.sql;

import java.time.format.DateTimeFormatter;

/**
 * Static class that provides constants used by SQL classes as well as the database.
 * @author Steven Hudson
 */
public class SQL {
    public static final String TBL_AUDITORIUMS = "auditoriums";
	public static final String TBL_FEATURE = "featuredata";
	public static final String TBL_PERFORMANCE = "performancedata";
	
	public static final String COL_UUID = "uuid";
	public static final String COL_CREATED = "created";
	public static final String COL_CHANGED = "changed";
	public static final String COL_ACTIVE = "active";
	
	public static final String COL_TITLE = "title";
	public static final String COL_RATING = "rating";
	public static final String COL_RUNTIME = "runtime";
	public static final String COL_IS3D = "is3d";
	public static final String COL_CC = "cc";
	public static final String COL_OC = "oc";
	public static final String COL_DA = "da";
	
	public static final String COL_FEATUREID = "feature";
	public static final String COL_DATETIME = "datetime";
	public static final String COL_AUDITORIUM = "auditorium";
	
	public static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private SQL() {}
}
