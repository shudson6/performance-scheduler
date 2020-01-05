package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import performancescheduler.TestData;
import performancescheduler.data.storage.MetaFeature;

public class SqlFeatureLoaderTest {
    @BeforeClass
    public static void before() throws ClassNotFoundException, IOException, SQLException {
        SqlTestUtil.insertTestData();
    }
    
    @Test
    public void test() throws ClassNotFoundException, SQLException, IOException {
        DbConnectionService dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
        Map<UUID, MetaFeature> ftrMap = new SqlFeatureLoader(TestData.TEST_TBL_FEATURE)
                .loadFeatures(dbcs.getStatement());
        assertEquals(2, ftrMap.size());
        assertTrue(ftrMap.containsValue(TestData.mfBar));
        assertTrue(ftrMap.containsValue(TestData.mfFoo));
    }
    
    @AfterClass
    public static void after() throws ClassNotFoundException, SQLException, IOException {
        SqlTestUtil.removeTestData();
    }
}
