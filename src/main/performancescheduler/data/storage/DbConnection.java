package performancescheduler.data.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.Rating;
import performancescheduler.entity.DataManager;
import performancescheduler.util.Context;

public class DbConnection {
    private final Connection connection;
    
    public static DbConnection getDatabase() {
        return new DbConnection();
    }
    
    public void loadFeatureData(DataManager<Feature> mgr) {
        mgr.addMapped(loadFeatureData());
    }
    
    public boolean saveFeatureData(DataManager<Feature> mgr) {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                // get the maps of all and updated data
                Map<UUID, Feature> all = mgr.getAllMapped();
                Map<UUID, Feature> upd8 = mgr.getUpdatedMapped();
                // start a string buffer for the insert statement.
                // note: update and delete statements will be added one-at-a-time while looping through maps
                StringBuilder insert = new StringBuilder("insert into featuredata values ");
                ArrayList<String> sqlList = new ArrayList<>();
                all.forEach((uuid, ftr) -> {
                    if (upd8.containsKey(uuid)) {
                        if (upd8.get(uuid) != null) {
                            sqlList.add(String.format("update featuredata set title='%s', rating='%s', runtime=%d, "
                                    + "is3d=%s, cc=%s, oc=%s, da=%s where uuid='%s';", ftr.getTitle(),
                                    ftr.getRating().toString(), ftr.getRuntime(), ftr.is3d(), ftr.hasClosedCaptions(),
                                    ftr.hasOpenCaptions(), ftr.hasDescriptiveAudio(), uuid));
                        } else {
                            // marked for deletion!
                            sqlList.add(String.format("delete from featuredata where uuid='%s';", uuid));
                        }
                    } else {
                        // add it to the insert statement
                        insert.append(String.format("('%s', '%s', '%s', %d, %s, %s, %s, %s), ", uuid,
                                ftr.getTitle(), ftr.getRating().toString(), ftr.getRuntime(),
                                Boolean.toString(ftr.is3d()), Boolean.toString(ftr.hasClosedCaptions()),
                                Boolean.toString(ftr.hasOpenCaptions()), Boolean.toString(ftr.hasDescriptiveAudio())));
                    }
                });
                // at this point, sqlList may contain some commands and insert may contain some stuff to insert
                // we will know if insert has anything if it contains a comma
                if (insert.lastIndexOf(",") != -1) {
                    insert.deleteCharAt(insert.lastIndexOf(","));
                    insert.append("on conflict do nothing;");
                    sqlList.add(insert.toString());
                }
                // sweet. so, now if sqlList is empty there is nothing to do, otherwise it contains valid commands
                if (!sqlList.isEmpty()) {
                    for (String sql : sqlList) {
                        stmt.addBatch(sql);
                    }
                    stmt.executeBatch();
                }
                return true;
            } catch (SQLException ex) {
                System.out.println("DbConnection: failed getting statement to save feature data: " + ex.getMessage());
            }
        }
        return false;
    }
    
    public boolean savePerformanceData(DataManager<Performance> mgr, DataManager<Feature> ftrMgr) {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                // get all and just updated data
                Map<UUID, Performance> all = mgr.getAllMapped();
                Map<UUID, Performance> upd8 = mgr.getUpdatedMapped();
                DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
                StringBuilder insert = new StringBuilder("insert into performancedata values ");
                ArrayList<String> sqlList = new ArrayList<>();
                all.forEach((uuid, perf) -> {
                    if (upd8.containsKey(uuid)) {
                        if (upd8.get(uuid) != null) {
                            sqlList.add(String.format("update performancedata set datetime='%s', auditorium=%d, "
                                    + "feature='%s' where uuid='%s';", perf.getDateTime().format(dateFmt),
                                    perf.getAuditorium().getNumber(), ftrMgr.getUUID(perf.getFeature()), uuid));
                        } else {
                            // marked for deletion!
                            sqlList.add(String.format("delete from performancedata where uuid='%s';", uuid));
                        }
                    } else {
                        // add to insert statement
                        insert.append(String.format("('%s', '%s', %d, '%s'), ", uuid, 
                                perf.getDateTime().format(dateFmt), perf.getAuditorium().getNumber(), 
                                ftrMgr.getUUID(perf.getFeature())));
                    }
                });
                if (insert.lastIndexOf(",") != -1) {
                    insert.deleteCharAt(insert.lastIndexOf(","));
                    insert.append("on conflict do nothing;");
                    sqlList.add(insert.toString());
                }
                if (!sqlList.isEmpty()) {
                    for (String sql : sqlList) {
                        stmt.addBatch(sql);
                    }
                    stmt.executeBatch();
                }
                return true;
            } catch (SQLException ex) {
                System.out.print("DbConnection: failed getting statement to save performance data: ");
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }
    
    private Map<UUID, Feature> loadFeatureData() {
        if (connection != null) {
            FeatureFactory factory = FeatureFactory.newFactory();
            ResultSet rs = null;
            try (Statement stmt = connection.createStatement()) {
                Map<UUID, Feature> ftrMap = new HashMap<>();
                String sql = "select * from featuredata;";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    String title = rs.getString("title");
                    Rating r = Rating.valueOf(rs.getString("rating"));
                    int run = rs.getInt("runtime");
                    boolean is3d = rs.getBoolean("is3d");
                    boolean cc = rs.getBoolean("cc");
                    boolean oc = rs.getBoolean("oc");
                    boolean da = rs.getBoolean("da");
                    Feature ftr = factory.createFeature(title, r, run, is3d, cc, oc, da);
                    ftrMap.put(uuid, ftr);
                }
                System.out.println("[DEBUG] loaded " + ftrMap.size() + " feature objects from database");
                return ftrMap;
            } catch (Exception ex) {
                System.err.println("DbConnection: failed to load feature data: ");
                System.out.println(ex.getMessage());
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException ex) {
                    // nothing to do here; the rs will be closed by the Statement above when it auto-closes;
                    // this finally block is likely unreachable
                }
            }
        }
        return null;
    }
    
    private DbConnection() {
        connection = getConnection();
    }
    
    private Connection getConnection() {
        Properties context = Context.getPresentContext();
        String url = context.getProperty("DATABASE_URL");
        String user = context.getProperty("DATABASE_USER");
        String passwd = context.getProperty("DATABASE_PASSWD");
        Connection conn = null;
        try {
            Class.forName(context.getProperty("DATABASE_CLASS"));
            conn = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException ex) {
            System.err.print("DbConnection: ClassNotFoundException caught loading database driver. ");
            System.err.println(" Could not start database connection.");
        } catch (SQLException ex) {
            System.err.print("DbConnection: SQLException caught while attemnpting connection to database: ");
            System.err.println(ex.getMessage());
        }
        return conn;
    }
    
    protected void finalize() {
        // let's not forget to close the connection!
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("DbConnection: Caught SQLException while closing database connection. Ignoring.");
        }
    }
}
