package performancescheduler.data.storage.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaDataFactory;
import performancescheduler.data.storage.MetaFeature;

public class SqlFeatureLoader {
    FeatureFactory featureFactory = FeatureFactory.newFactory();
    MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    
    public Map<UUID, MetaFeature> loadFeatures(Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery("select count(uuid) from featuredata where active=true;");
        Map<UUID, MetaFeature> ftrMap = new HashMap<>(rs.getInt(1) * 2);
        rs = stmt.executeQuery("select * from featuredata where active=true;");
        while (rs.next()) {
            ftrMap.put(UUID.fromString(rs.getString(SQL.COL_UUID)), createMetaFeature(rs));
        }
        return ftrMap;
    }
    
    private MetaFeature createMetaFeature(ResultSet rs) throws SQLException {
        return metaFactory.newMetaFeature(createFeature(rs), UUID.fromString(rs.getString(SQL.COL_UUID)),
                LocalDateTime.parse(rs.getString(SQL.COL_CREATED)), LocalDateTime.parse(rs.getString(SQL.COL_CHANGED)));
    }
    
    private Feature createFeature(ResultSet rs) throws SQLException {
        return featureFactory.createFeature(rs.getString(SQL.COL_TITLE), Rating.valueOf(rs.getString(SQL.COL_RATING)),
                rs.getInt(SQL.COL_RUNTIME), rs.getBoolean(SQL.COL_IS3D), rs.getBoolean(SQL.COL_CC),
                rs.getBoolean(SQL.COL_OC), rs.getBoolean(SQL.COL_DA));
    }
}
