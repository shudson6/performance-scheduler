package performancescheduler.data.storage.sql;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.storage.MetaPerformance;
import performancescheduler.data.storage.TestMetaPerformance;
import performancescheduler.util.UUIDGenerator;

public class PerformanceValueListerTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void test() {
        Performance p = PerformanceFactory.newFactory().createPerformance(null, LocalDateTime.now(), 1, 0, 0, 0);
        MetaPerformance mp = new TestMetaPerformance(p, new UUIDGenerator().generateUUID(), LocalDateTime.now(), null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MetaPerformance");
        new PerformanceValueLister().colValue(SQL.COL_FEATUREID, mp);
    }

}
