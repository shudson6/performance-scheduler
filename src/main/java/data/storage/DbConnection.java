package main.java.data.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import main.java.incomplete.Context;

public class DbConnection {
    private final Connection connection;
    
    public static DbConnection getDatabase() {
        return new DbConnection();
    }
    
    private DbConnection() {
        connection = getConnection();
    }
    
    private Connection getConnection() {
        Properties context = Context.getPresentContext();
        String url = context.getProperty("DATABASE_URL");
        String user = context.getProperty("DATABASE_USER");
        String passwd = context.getProperty("DATABASE_PASSWD");
        try {
            Class.forName(context.getProperty("DATABASE_CLASS"));
            Connection conn = DriverManager.getConnection(url, user, passwd);
            return conn;
        } catch (ClassNotFoundException ex) {
            System.err.print("DbConnection: ClassNotFoundException caught loading database driver. ");
            System.err.println(" Could not start database connection.");
        } catch (SQLException ex) {
            System.err.print("DbConnection: SQLException caught while attemnpting connection to database: ");
            System.err.println(ex.getMessage());
        }
        return null;
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
