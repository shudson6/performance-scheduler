package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

class SqlSaver {
    private final DbConnectionService dbcs;
    
    public SqlSaver(DbConnectionService connection) {
        Objects.requireNonNull(connection);
        dbcs = connection;
    }
    
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
