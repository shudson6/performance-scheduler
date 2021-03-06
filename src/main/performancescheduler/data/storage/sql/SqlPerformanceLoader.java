package performancescheduler.data.storage.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.storage.MetaDataFactory;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

/**
 * Class used by {@link SqlLoader} to load performance data.
 * @author Steven Hudson
 */
public class SqlPerformanceLoader {
    private PerformanceFactory perfFactory = PerformanceFactory.newFactory();
    private MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    private final String table;
    
    /**
     * Create a new instance.
     * @param pfmTableName the name of the database table containing performance data
     */
    public SqlPerformanceLoader(String pfmTableName) {
        Objects.requireNonNull(pfmTableName);
        table = pfmTableName;
    }
    
    /**
     * Loads performance data. For each performance entry loaded, the created instance will contain the feature
     * mapped to the UUID referred to by the entry's 'feature' column.
     * 
     * @param stmt {@code Statement} to execute SQL queries
     * @param ftrs mapping of feature instances to their UUIDs
     * @param start beginning of the valid date range (inclusive)
     * @param end of the valid date range (exclusive)
     * @return collection of new {@code MetaPerformance} instances representing the loaded entries
     * @throws SQLException if an exception occurs while reading data
     */
    public Collection<MetaPerformance> loadPerformances(Statement stmt, Map<UUID, MetaFeature> ftrs,
            LocalDate start, LocalDate end) throws SQLException {
        Collection<MetaPerformance> pfms = createCollection(stmt.executeQuery(countCmd(start, end)));
        ResultSet rs = stmt.executeQuery(dataCmd(start, end));
        while (rs.next()) {
            pfms.add(createMetaPerformance(rs, ftrs));
        }
        return pfms;
    }
    
    private Collection<MetaPerformance> createCollection(ResultSet rs) throws SQLException {
        rs.next();
        return new ArrayList<>(rs.getInt(1));
    }
    
    private MetaPerformance createMetaPerformance(ResultSet rs, Map<UUID, MetaFeature> ftrs) throws SQLException {
        return metaFactory.newMetaPerformance(createPerformance(rs, ftrs), UUID.fromString(rs.getString(SQL.COL_UUID)),
                LocalDateTime.parse(rs.getString(SQL.COL_CREATED), SQL.DATETIME_FMT),
                LocalDateTime.parse(rs.getString(SQL.COL_CHANGED), SQL.DATETIME_FMT));
    }
    
    private Performance createPerformance(ResultSet rs, Map<UUID, MetaFeature> ftrs) throws SQLException {
        return perfFactory.createPerformance(ftrs.get(UUID.fromString(rs.getString(SQL.COL_FEATUREID))),
                LocalDateTime.parse(rs.getString(SQL.COL_DATETIME), SQL.DATETIME_FMT),
                rs.getInt(SQL.COL_AUDITORIUM), rs.getInt(SQL.COL_SEATING), rs.getInt(SQL.COL_CLEANUP),
                rs.getInt(SQL.COL_TRAILER));
    }
    
    private String dataCmd(LocalDate start, LocalDate end) {
    	return command("*", start, end);
    }
    
    private String countCmd(LocalDate start, LocalDate end) {
    	return command(String.format("COUNT(%s)", SQL.COL_UUID), start, end);
    }
    
    private String command(String select, LocalDate start, LocalDate end) {
        // not checked; the loader will only call this with valid dates
    	return String.format("SELECT %s FROM %s WHERE %s=TRUE AND %s>=CAST('%s' AS DATE) AND %s<=CAST('%s' AS DATE);",
    			select, table, SQL.COL_ACTIVE, SQL.COL_DATETIME, start.format(SQL.DATE_FMT),
    			SQL.COL_DATETIME, end.format(SQL.DATE_FMT));
    }
}
