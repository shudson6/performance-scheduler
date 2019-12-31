package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.Auditorium;
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
        Performance p = PerformanceFactory.newFactory().createPerformance(null, LocalDateTime.now(),
                Auditorium.getInstance(1, null, false, 100));
        MetaPerformance mp = new TestMetaPerformance(p, new UUIDGenerator().generateUUID(), LocalDateTime.now(), null);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MetaPerformance");
        new PerformanceValueLister().colValue(SQL.COL_FEATUREID, mp);
    }

}
