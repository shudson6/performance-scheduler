package performancescheduler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.Rating;

public class PerformanceManagerTest {
    private PerformanceManager mgr = new PerformanceManager();
    private Feature ftr = FeatureFactory.newFactory().createFeature("Foo", Rating.PG, 90, false, false, false, false);
    private Performance p = mgr.getPerformanceFactory().createPerformance(ftr, LocalDateTime.now(), 1);
    
    @Test
    public void testGetData() {
        mgr.add(p);
        assertEquals(1, mgr.size());
        assertTrue(mgr.getData().contains(p));
    }
}