package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlLoader {
    private final DbConnectionService dbcs;
    
    public SqlLoader(DbConnectionService connection) {
        Objects.requireNonNull(connection);
        dbcs = connection;
    }
    
    public void load(Collection<? super MetaFeature> features, Collection<? super MetaPerformance> performances,
            LocalDate start, LocalDate end) throws IOException, SQLException {
        // can never load performances w/o features (dependency)
        Objects.requireNonNull(features);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        LocalDate openDate = (start.compareTo(end) <= 0) ? start : end;
        LocalDate closeDate = (end.compareTo(start) >= 0) ? end : start;
        features.clear();
        Map<UUID, MetaFeature> ftrMap = new SqlFeatureLoader(dbcs.getProperty("features"))
        		.loadFeatures(dbcs.getStatement());
        features.addAll(ftrMap.values());
        if (performances != null) {
        	performances.clear();
        	performances.addAll(new SqlPerformanceLoader(dbcs.getProperty("performances"))
        			.loadPerformances(dbcs.getStatement(), ftrMap, openDate, closeDate));
        }
    }
}
