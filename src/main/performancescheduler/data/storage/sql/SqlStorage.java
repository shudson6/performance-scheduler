package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlStorage {
    protected DbConnectionService dbcs;
    
    public SqlStorage(Properties props) throws ClassNotFoundException, SQLException {
    	dbcs = new DbConnectionService(props);
    	dbcs.start();
    }

    public void restore(Collection<? super MetaFeature> featureData, Collection<? super MetaPerformance> perfData,
    		LocalDate start, LocalDate end) throws IOException, SQLException {
        Objects.requireNonNull(featureData);
        new SqlLoader(dbcs).load(featureData, perfData, start, end);
    }

    public void store(Collection<MetaFeature> featureData, Collection<MetaPerformance> performanceData) 
            throws IOException {
        new SqlSaver(dbcs).save(featureData, performanceData);
    }
}
