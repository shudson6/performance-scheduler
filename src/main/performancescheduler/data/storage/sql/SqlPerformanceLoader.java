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
import performancescheduler.util.Context;

public class SqlPerformanceLoader {
    private PerformanceFactory perfFactory = PerformanceFactory.newFactory();
    private MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    private final String table;
    
    public SqlPerformanceLoader(String pfmTableName) {
        Objects.requireNonNull(pfmTableName);
        table = pfmTableName;
    }
    
    public Collection<MetaPerformance> loadPerformances(Statement stmt, Map<UUID, MetaFeature> ftrs,
            LocalDate start, LocalDate end) throws SQLException {
        Collection<MetaPerformance> pfms = createCollection(stmt);
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s WHERE %s=TRUE;", table, SQL.COL_ACTIVE));
        while (rs.next()) {
            pfms.add(createMetaPerformance(rs, ftrs));
        }
        return pfms;
    }
    
    private Collection<MetaPerformance> createCollection(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery(String.format("SELECT COUNT(%s) FROM %s WHERE %s=TRUE;",
                SQL.COL_UUID, table, SQL.COL_ACTIVE));
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
                Context.getAuditorium(rs.getInt(SQL.COL_AUDITORIUM)));
    }
    
    private String dataCmd(LocalDate start, LocalDate end) {
        // not checked; the loader will only call this with valid dates
    }
}
