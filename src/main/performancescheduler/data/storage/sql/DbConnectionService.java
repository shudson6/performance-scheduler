package performancescheduler.data.storage.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import performancescheduler.util.Context;

class DbConnectionService implements AutoCloseable {
    private Connection conn = null;
    
    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
    
    public Statement getStatement() throws IOException {
        if (conn != null) {
            try {
                return conn.createStatement();
            } catch (SQLException e) {
                throw new IOException("Could not get Statement object: see cause", e);
            }
        } else {
            throw new IOException("Cannot create Statement; no connection to database.");
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
    
    static {
        try {
            Class.forName(Context.getProperty("DB_DRIVER"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
