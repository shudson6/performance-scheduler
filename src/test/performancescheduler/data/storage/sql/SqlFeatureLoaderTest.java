package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import performancescheduler.data.storage.MetaFeature;

public class SqlFeatureLoaderTest {

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

}
