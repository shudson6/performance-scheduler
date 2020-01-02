package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import performancescheduler.util.Context;

class DbConnectionService implements AutoCloseable {
    private Connection conn = null;
    private Properties dbProperties;
    
    public DbConnectionService(Properties p) throws ClassNotFoundException {
        dbProperties = validateProperties(p);
        loadDefaultDriver();
    }
    
    public void close() throws SQLException {
        if (conn != null) {
            try {
                conn.close();
            } finally {
                conn = null;
            }
        }
    }
    
    public String getProperty(String propName) {
        return dbProperties.getProperty(propName);
    }
    
    public Statement getStatement() throws IOException {
        try {
            return conn.createStatement();
        } catch (Exception ex) {
            throw new IOException("Failed to create Statement object: see cause.", ex);
        }
    }
    
    public boolean isStarted() {
        return (conn != null);
    }
    
    public void start() throws SQLException  {
        conn = DriverManager.getConnection(dbProperties.getProperty("url"), dbProperties);
    }
    
    public static Properties getDefaultProperties() {
        Properties prop = new Properties();
        prop.putAll(defaults);
        return prop;
    }
    
    static void loadDefaultDriver() throws ClassNotFoundException {
        loadDriver(Context.getProperty("DB_DRIVER"));
    }
    
    static void loadDriver(String className) throws ClassNotFoundException {
        Class.forName(className);
    }
    
    static Properties validateProperties(Properties p) {
        Properties prop = new Properties(defaults);
        if (p != null) {
            prop.putAll(p);
        }
        return prop;
    }
    
    private static final Properties defaults = new Properties();
    static {
        defaults.put("url", Context.getProperty("DB_URL"));
        defaults.put("user", Context.getProperty("DB_USER"));
        defaults.put("password", Context.getProperty("DB_PASSWD"));
        defaults.put("features", SQL.TBL_FEATURE);
        defaults.put("performances", SQL.TBL_PERFORMANCE);
        defaults.put("auditoriums", SQL.TBL_AUDITORIUMS);
    }
}
