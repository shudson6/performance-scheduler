package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import performancescheduler.util.Context;

class DbConnectionService implements AutoCloseable {
    private Connection conn = null;
    
    public DbConnectionService() throws ClassNotFoundException {
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
        conn = DriverManager.getConnection(Context.getProperty("DB_URL"),
                                           Context.getProperty("DB_USER"),
                                           Context.getProperty("DB_PASSWD"));
    }
    
    static void loadDefaultDriver() throws ClassNotFoundException {
        loadDriver(Context.getProperty("DB_DRIVER"));
    }
    
    static void loadDriver(String className) throws ClassNotFoundException {
        Class.forName(className);
    }
}
