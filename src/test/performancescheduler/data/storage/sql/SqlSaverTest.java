package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class SqlSaverTest {
    static DbConnectionService dcs;
    
    @BeforeClass
    public static void setUpBefore() throws ClassNotFoundException {
        dcs = new DbConnectionService();
    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
