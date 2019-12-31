package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.util.Context;

public class DbConnectionServiceTest {
    DbConnectionService dcs = null;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Before
    public void setUp() throws ClassNotFoundException {
        dcs = new DbConnectionService(null);
    }
    
    @After
    public void tearDown() throws SQLException {
        if (dcs != null && dcs.isStarted()) {
            dcs.close();
        }
    }
    
    @Test
    public void closeIsNopIfConnNull() throws SQLException, ClassNotFoundException {
        new DbConnectionService(null).close();
    }

    @Test
    public void startThenCloseService() throws SQLException {
        dcs.start();
        assertTrue(dcs.isStarted());
        dcs.close();
        assertFalse(dcs.isStarted());
    }
    
    @Test
    public void getIOExceptionWhenNotStarted() throws IOException {
        assertFalse(dcs.isStarted());
        exception.expect(IOException.class);
        dcs.getStatement();
    }
    
    @Test
    public void shouldGetAStatement() throws SQLException, IOException {
        Statement s = null;
        dcs.start();
        s = dcs.getStatement();
        assertNotNull(s);
    }
    
    @Test
    public void shouldGetClassNotFoundException() throws ClassNotFoundException {
        exception.expect(ClassNotFoundException.class);
        DbConnectionService.loadDriver("foobar");
    }
    
    @Test
    public void checkDefaultProperties() {
        assertEquals(Context.getProperty("DB_URL"), DbConnectionService.getDefaultProperties().getProperty("url"));
        assertEquals(Context.getProperty("DB_USER"), DbConnectionService.getDefaultProperties().getProperty("user"));
        assertEquals(Context.getProperty("DB_PASSWD"), DbConnectionService.getDefaultProperties()
                .getProperty("password"));
        assertEquals(SQL.TBL_FEATURE, DbConnectionService.getDefaultProperties().getProperty("features"));
        assertEquals(SQL.TBL_PERFORMANCE, DbConnectionService.getDefaultProperties().getProperty("performances"));
    }
    
    @Test
    public void checkCustomProperties() throws ClassNotFoundException, SQLException {
        Properties props = new Properties();
        props.put("test", "testy");
        DbConnectionService d = new DbConnectionService(props);
        assertEquals("testy", d.getProperty("test"));
        d.close();
    }
}
