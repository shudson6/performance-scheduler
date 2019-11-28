package test.java.data.storage;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.data.storage.DbConnection;

public class DbConnectionTest {

    @Test
    public void test() throws Exception {
        DbConnection.getDatabase();
    }

}
