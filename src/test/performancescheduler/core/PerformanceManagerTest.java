package performancescheduler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import performancescheduler.TestData;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.Rating;

public class PerformanceManagerTest {
    private PerformanceDataModel mgr = new PerformanceDataModel();
    private Feature ftr = TestData.featureFactory.createFeature("Foo", Rating.PG, 90, false, false, false, false);
    private Performance p = TestData.performanceFactory.createPerformance(ftr, LocalDateTime.now(), 1);
    
    @Test
    public void testGetData() {
        mgr.add(p);
        assertEquals(1, mgr.size());
        assertTrue(mgr.getData().contains(p));
    }
}
