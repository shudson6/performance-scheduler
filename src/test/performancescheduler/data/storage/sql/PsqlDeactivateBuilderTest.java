package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Test;

import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;
import performancescheduler.data.storage.MetaWrapper;
import performancescheduler.data.storage.TestMetaFeature;
import performancescheduler.data.storage.TestMetaPerformance;

public class PsqlDeactivateBuilderTest {
    FeatureFactory ftrFactory = FeatureFactory.newFactory();
    UUID uuid = new UUID(0xabcd987612341234L, 0x1234abcdef567890L);
    LocalDateTime ldt = LocalDateTime.of(2020, 3, 27, 19, 15);
    PsqlDeactivateBuilder pDel = new PsqlDeactivateBuilder(TestData.TEST_TBL_FEATURE, TestData.TEST_TBL_PERFORMANCE);

    @Test
    public void emptyCommand() {
        pDel.clear();
        assertFalse(pDel.add(null));
        MetaFeature mf = new TestMetaFeature(ftrFactory.createFeature("foo", Rating.NR, 90, false, true, true, true),
                uuid, ldt, null);
        assertFalse(pDel.add(mf));
        assertTrue(pDel.getCommand().isEmpty());
    }

    @Test
    public void deleteFeatureAndPerformanceButNotString() {
        pDel.clear();
        MetaFeature mf = new TestMetaFeature(null, uuid, ldt, null);
        MetaPerformance mp = new TestMetaPerformance(null, uuid, ldt, null);
        assertTrue(pDel.add(mf));
        assertTrue(pDel.add(mp));
        assertTrue(pDel.add(new MetaWrapper<String>(null, uuid, ldt, null)));
        assertEquals(delstr(mf) + delstr(mp), pDel.getCommand());
    }
    
    private String delstr(MetaWrapper<?> mw) {
        return String.format("UPDATE %s SET %s=false,%s='%s' WHERE %s='%s';", 
                (mw instanceof MetaPerformance) ? TestData.TEST_TBL_PERFORMANCE : TestData.TEST_TBL_FEATURE,
                SQL.COL_ACTIVE, SQL.COL_CHANGED, mw.getChangedTimestamp().format(SQL.DATETIME_FMT),
                SQL.COL_UUID, mw.getUuid().toString());
    }
}
