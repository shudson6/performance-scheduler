package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.TestData;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlPerformanceLoaderTest {
	static LocalDateTime early = LocalDateTime.of(1900, 10, 19, 13, 37);
	static LocalDateTime late = LocalDateTime.of(2020, 1, 1, 13, 37);

    @BeforeClass
    public static void before() throws ClassNotFoundException, IOException, SQLException {
        SqlTestUtil.insertTestData();
    }
    
    @AfterClass
    public static void after() throws ClassNotFoundException, SQLException, IOException {
        SqlTestUtil.removeTestData();
    }
    
    @Test
    public void test() throws ClassNotFoundException, SQLException, IOException {
        DbConnectionService dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
        Map<UUID, MetaFeature> ftrMap = new SqlFeatureLoader(dbcs.getProperty("features"))
                .loadFeatures(dbcs.getStatement());
        Collection<MetaPerformance> perf = new SqlPerformanceLoader(dbcs.getProperty("performances"))
                .loadPerformances(dbcs.getStatement(), ftrMap, early.toLocalDate(), late.toLocalDate());
        assertEquals(2, perf.size());
        assertTrue(perf.contains(TestData.mpFoo1));
        assertTrue(perf.contains(TestData.mpBar2));
        dbcs.close();
    }
}
