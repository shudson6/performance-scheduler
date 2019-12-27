package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.util.Collection;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlStorage {
    protected DbConnectionService dcs;

    public void restore(Collection<MetaFeature> featureData, Collection<MetaPerformance> performanceData)
            throws IOException {
        
    }

    public void store(Collection<MetaFeature> featureData, Collection<MetaPerformance> performanceData) 
            throws IOException {
        new SqlSaver(dcs).save(featureData, performanceData);
    }
}
