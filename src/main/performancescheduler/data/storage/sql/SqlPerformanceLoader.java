package performancescheduler.data.storage.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.storage.MetaDataFactory;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;
import performancescheduler.util.Context;

public class SqlPerformanceLoader {
    PerformanceFactory perfFactory = PerformanceFactory.newFactory();
    MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    
    public Collection<MetaPerformance> loadPerformances(Statement stmt, Map<UUID, MetaFeature> ftrs) throws SQLException {
        Collection<MetaPerformance> pfms = createCollection(stmt);
        ResultSet rs = stmt.executeQuery("select * from performancedata where active=true;");
        while (rs.next()) {
            pfms.add(createMetaPerformance(rs, ftrs));
        }
        return pfms;
    }
    
    private Collection<MetaPerformance> createCollection(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery("select count(uuid) from performancedata where active=true;");
        return new ArrayList<>(rs.getInt(1));
    }
    
    private MetaPerformance createMetaPerformance(ResultSet rs, Map<UUID, MetaFeature> ftrs) throws SQLException {
        return metaFactory.newMetaPerformance(createPerformance(rs, ftrs), UUID.fromString(rs.getString(SQL.COL_UUID)),
                LocalDateTime.parse(rs.getString(SQL.COL_CREATED)), LocalDateTime.parse(rs.getString(SQL.COL_CHANGED)));
    }
    
    private Performance createPerformance(ResultSet rs, Map<UUID, MetaFeature> ftrs) throws SQLException {
        return perfFactory.createPerformance(ftrs.get(UUID.fromString(rs.getString(SQL.COL_UUID))),
                LocalDateTime.parse(rs.getString(SQL.COL_DATETIME)),
                Context.getAuditorium(rs.getInt(SQL.COL_AUDITORIUM)));
    }
}
