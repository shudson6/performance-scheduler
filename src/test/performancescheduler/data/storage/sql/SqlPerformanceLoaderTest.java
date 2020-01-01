package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlPerformanceLoaderTest {

    @Test
    public void test() throws ClassNotFoundException, SQLException, IOException {
        DbConnectionService dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
        Map<UUID, MetaFeature> ftrMap = new SqlFeatureLoader(dbcs.getProperty("features"))
                .loadFeatures(dbcs.getStatement());
        Collection<MetaPerformance> perf = new SqlPerformanceLoader(dbcs.getProperty("performances"))
                .loadPerformances(dbcs.getStatement(), ftrMap);
        assertEquals(2, perf.size());
        assertTrue(perf.contains(TestData.mpFoo1));
        assertTrue(perf.contains(TestData.mpBar2));
    }

}
