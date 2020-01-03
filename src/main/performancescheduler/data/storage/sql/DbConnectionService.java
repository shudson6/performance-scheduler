package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import performancescheduler.util.Context;

/**
 * Class used to connect to the database server.
 * @author Steven Hudson
 */
class DbConnectionService implements AutoCloseable {
    private Connection conn = null;
    private Properties dbProperties;
    
    /**
     * Create a new instance. Optionally, properties may be provided to override certain defaults as specified in
     * {@link #getDefaultProperties()}.
     * @param p optional properties (may be null).
     * @throws ClassNotFoundException
     */
    public DbConnectionService(Properties p) throws ClassNotFoundException {
        dbProperties = validateProperties(p);
        loadDefaultDriver();
    }
    
    /**
     * Close the database connection. Note that another connection may be opened with another call to {@link #start()}.
     */
    public void close() throws SQLException {
        if (conn != null) {
            try {
                conn.close();
            } finally {
                conn = null;
            }
        }
    }
    
    /**
     * Get a property belonging to this instance.
     * @param propName the desired property
     * @return the value for that property
     */
    public String getProperty(String propName) {
        return dbProperties.getProperty(propName);
    }
    
    /**
     * Get a {@link Statement} instance from the database connection.
     * @return {@code Statement} as returned by {@link Connection#createStatement()}
     * @throws IOException
     */
    public Statement getStatement() throws IOException {
        try {
            return conn.createStatement();
        } catch (Exception ex) {
            throw new IOException("Failed to create Statement object: see cause.", ex);
        }
    }
    
    /**
     * Determine if this instance is started.
     * @return {@code true} if connected to the database server
     * @see {@link #start()}
     */
    public boolean isStarted() {
        return (conn != null);
    }
    
    /**
     * Start this {@code DbConnectionService}. That is, establish a connection to the database.
     * @throws SQLException
     */
    public void start() throws SQLException  {
        conn = DriverManager.getConnection(dbProperties.getProperty("url"), dbProperties);
    }
    
    /**
     * Get a copy of the default properties. Default properties include:<br>
     * url - the url of the database to connect<br>
     * user - the user or role to authenticate<br>
     * password - to authenticate<br>
     * features - name of the table where features are stored<br>
     * performances - name of the table where performances are stored<br>
     * auditoriums - name of the table where auditoriums are stored
     * 
     * @return new {@code Properties} instance containing default values.
     */
    public static Properties getDefaultProperties() {
        Properties prop = new Properties(defaults);
        return prop;
    }
    
    /**
     * Loads the default driver class.
     * @throws ClassNotFoundException
     * @see {@link #loadDriver(String)}
     */
    static void loadDefaultDriver() throws ClassNotFoundException {
        loadDriver(defaults.getProperty("driver"));
    }
    
    /**
     * Loads a driver class to establish communication with a database server.
     * @param className the name of the desired driver class
     * @throws ClassNotFoundException
     */
    static void loadDriver(String className) throws ClassNotFoundException {
        Class.forName(className);
    }
    
    /**
     * Creates a valid (that is, usable by this class) Properties object based on the one provided and including
     * default values.
     * 
     * @param p input {@code Properties}
     * @return validated {@code Properties} object
     */
    static Properties validateProperties(Properties p) {
        Properties prop = new Properties(defaults);
        if (p != null) {
            prop.putAll(p);
        }
        return prop;
    }
    
    private static final Properties defaults = new Properties();
    static {
    	defaults.put("driver", "org.postgresql.Driver");
        defaults.put("url", "jdbc:postgresql://localhost/perfsched");
        defaults.put("user", "perfsched");
        defaults.put("password", "hudysched");
        defaults.put("features", SQL.TBL_FEATURE);
        defaults.put("performances", SQL.TBL_PERFORMANCE);
        defaults.put("auditoriums", SQL.TBL_AUDITORIUMS);
    }
}
