package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DbConnectionServiceTest {
    DbConnectionService dcs = null;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Before
    public void setUp() throws ClassNotFoundException {
        dcs = new DbConnectionService();
    }
    
    @After
    public void tearDown() throws SQLException {
        if (dcs != null && dcs.isStarted()) {
            dcs.close();
        }
    }
    
    @Test
    public void closeIsNopIfConnNull() throws SQLException, ClassNotFoundException {
        new DbConnectionService().close();
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
}
