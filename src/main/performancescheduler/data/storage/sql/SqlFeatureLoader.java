package performancescheduler.data.storage.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaDataFactory;
import performancescheduler.data.storage.MetaFeature;

/**
 * Class used by {@link SqlLoader} to load feature data
 * @author Steven Hudson
 */
public class SqlFeatureLoader {
    private FeatureFactory featureFactory = FeatureFactory.newFactory();
    private MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    private String table;
    
    /**
     * Create a new instance.
     * @param ftrTableName name of the database table containing feature data
     */
    public SqlFeatureLoader(String ftrTableName) {
        Objects.requireNonNull(ftrTableName);
        table = ftrTableName;
    }
    
    /**
     * Loads feature data from the database and returns a mapping of feature instances to their UUIDs.
     * 
     * @param stmt for executing SQL queries
     * @return loaded features mapped to UUIDs
     * @throws SQLException if an exception occurs while reading data
     */
    public Map<UUID, MetaFeature> loadFeatures(Statement stmt) throws SQLException {
        Map<UUID, MetaFeature> ftrMap = new HashMap<>(hashMapCap(stmt));
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s WHERE %s=TRUE;", table, SQL.COL_ACTIVE));
        while (rs.next()) {
            ftrMap.put(UUID.fromString(rs.getString(SQL.COL_UUID)), createMetaFeature(rs));
        }
        return ftrMap;
    }
    
    private MetaFeature createMetaFeature(ResultSet rs) throws SQLException {
        return metaFactory.newMetaFeature(createFeature(rs), UUID.fromString(rs.getString(SQL.COL_UUID)),
                LocalDateTime.parse(rs.getString(SQL.COL_CREATED), SQL.DATETIME_FMT),
                LocalDateTime.parse(rs.getString(SQL.COL_CHANGED), SQL.DATETIME_FMT));
    }
    
    private Feature createFeature(ResultSet rs) throws SQLException {
        return featureFactory.createFeature(rs.getString(SQL.COL_TITLE), Rating.valueOf(rs.getString(SQL.COL_RATING)),
                rs.getInt(SQL.COL_RUNTIME), rs.getBoolean(SQL.COL_IS3D), rs.getBoolean(SQL.COL_CC),
                rs.getBoolean(SQL.COL_OC), rs.getBoolean(SQL.COL_DA));
    }
    
    private int hashMapCap(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery(String.format("SELECT COUNT(%s) FROM %s WHERE %s=TRUE;",
                SQL.COL_UUID, table, SQL.COL_ACTIVE));
        rs.next();
        return rs.getInt(1) * 2;
    }
}
