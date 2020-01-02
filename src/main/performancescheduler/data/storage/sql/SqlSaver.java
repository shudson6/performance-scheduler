package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

/**
 * Class used by {@link SqlSaver} to save feature and performance data.
 * 
 * @author Steven Hudson
 */
class SqlSaver {
    private final DbConnectionService dbcs;
    
    /**
     * Create a new instance.
     * 
     * @param connection the {@link DbConnectionService} which serves this instance
     */
    public SqlSaver(DbConnectionService connection) {
        Objects.requireNonNull(connection);
        dbcs = connection;
    }
    
    /**
     * Saves the provided data to the database. Where elements already exist in the database, determined by checking
     * UUID, relevant entries are updated. If an element's UUID exists in the database but the instance wraps
     * {@code null}, indicating the object was "deleted," that entry's {@code active} column is updated to
     * {@code false}. Either parameter may be {@code null}, however, in such event there may be a risk of exception
     * e.g. if a performance refers to a feature which does not exist in the database.
     * 
     * @param features the features to be saved
     * @param performances the performances to be saved
     * @throws IOException if an exception occurs during saving
     */
    public void save(Collection<MetaFeature> features, Collection<MetaPerformance> performances) throws IOException {
        PsqlDeactivateBuilder delete = new PsqlDeactivateBuilder(dbcs.getProperty("features"),
                dbcs.getProperty("performances"));
        // delete performances first, since they have dependencies (don't assume ON DELETE CASCADE)
        PsqlInsertPerformanceBuilder pInsert = new PsqlInsertPerformanceBuilder(dbcs.getProperty("performances"));
        if (performances != null) {
            performances.stream().filter(p -> p.getWrapped() != null).forEach(pInsert::add);
            performances.stream().filter(p -> p.getWrapped() == null).forEach(delete::add);
        }
        PsqlInsertBuilder<MetaFeature> fInsert = new PsqlInsertBuilder<>(new FeatureValueLister(),
                dbcs.getProperty("features"));
        if (features != null) {
            features.stream().filter(mf -> mf.getWrapped() != null).forEach(fInsert::add);
            features.stream().filter(mf -> mf.getWrapped() == null).forEach(delete::add);
        }
        try {
            String cmd = delete.getCommand();
            if (!cmd.isEmpty()) {
                dbcs.getStatement().execute(cmd);
            }
            cmd = fInsert.getCommand();
            if (!cmd.isEmpty()) {
                dbcs.getStatement().execute(cmd);
            }
            cmd = pInsert.getCommand();
            if (!cmd.isEmpty()) {
                dbcs.getStatement().execute(cmd);
            }
        } catch (IOException | SQLException ex) {
            throw new IOException("Exception occurred while saving data.", ex);
        }
    }
}
