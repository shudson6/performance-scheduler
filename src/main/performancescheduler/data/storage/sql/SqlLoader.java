package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlLoader {
    private final DbConnectionService dcs;
    
    public SqlLoader(DbConnectionService connection) {
        Objects.requireNonNull(connection);
        dcs = connection;
    }
    
    public void load(Collection<MetaFeature> features, Collection<MetaPerformance> performances,
            LocalDate start, LocalDate end) throws IOException {
        // can never load performances w/o features (dependency)
        Objects.requireNonNull(features);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        LocalDate openDate = (start.compareTo(end) <= 0) ? start : end;
        LocalDate closeDate = (end.compareTo(start) >= 0) ? end : start;
    }
}
