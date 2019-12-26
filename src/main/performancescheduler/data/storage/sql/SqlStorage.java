package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;
import performancescheduler.data.storage.MetaWrapper;
import performancescheduler.data.storage.Storage;

public class SqlStorage implements Storage {
    protected final DbConnectionService dcs;
    
    public SqlStorage() {
        dcs = new DbConnectionService();
    }

    @Override
    public void restore(Collection<Feature> featureData, Collection<Performance> performanceData) throws IOException {
        
    }

    @Override
    public void store(Collection<Feature> featureData, Collection<Performance> performanceData) throws IOException {
        insertFeatures(featureData);
        insertPerformances(performanceData);
    }
    
    private void insertFeatures(Collection<Feature> featureData) throws IOException {
        PsqlInsertBuilder<MetaFeature> mfInsert = new PsqlInsertBuilder<>(new FeatureValueLister(), SQL.TBL_FEATURE);
        mfInsert.addAll(downCastCollection(featureData, new ArrayList<MetaFeature>()));
        try {
            statement().execute(mfInsert.getCommand());
        } catch (SQLException e) {
            throw new IOException("Failed to insert feature data: see cause", e);
        }
    }
    
    private void insertPerformances(Collection<Performance> pData) throws IOException {
        PsqlInsertPerformanceBuilder mpInsert = new PsqlInsertPerformanceBuilder();
        mpInsert.addAll(downCastCollection(pData, new ArrayList<MetaPerformance>()));
        try {
            statement().execute(mpInsert.getCommand());
        } catch (SQLException e) {
            throw new IOException("Failed to insert performance data: see cause", e);
        }
    }

    private Statement statement() throws IOException {
        if (dcs.isStarted()) {
            return dcs.getStatement();
        } else {
            throw new IOException("Database connection not started.");
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T, E extends MetaWrapper<T>> Collection<E> downCastCollection(Collection<T> coll, Collection<E> cast) {
        for (T t : coll) {
            if (t instanceof MetaWrapper<?>) {
                cast.add((E) t);
            }
        }
        return cast;
    }
}
