package performancescheduler.data.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;

public class MetaPerformanceTest {
    FeatureFactory ftrFactory;
    PerformanceFactory perfFactory;
    MetaDataFactory metaFactory;
    Feature ftr1;
    Feature ftr2;
    LocalDateTime dateTime;
    Auditorium aud;
    Performance p1;
    Performance p2;

    @Before
    public void setUp() {
        ftrFactory = FeatureFactory.newFactory();
        perfFactory = PerformanceFactory.newFactory();
        metaFactory = MetaDataFactory.newFactory();
        ftr1 = ftrFactory.createFeature("Foo", Rating.R, 90, false, false, false, false);
        ftr2 = ftrFactory.createFeature("Bar", Rating.R, 90, false, false, false, false);
        dateTime = LocalDateTime.now();
        aud = Auditorium.getInstance(1, null, false, 100);
        p1 = perfFactory.createPerformance(ftr1, dateTime, aud);
        p2 = perfFactory.createPerformance(ftr2, dateTime, aud);
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void wrappingNullCausesNPE() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("wrapped data");
        metaFactory.newMetaPerformance(null);
    }
    
    @Test
    public void verifyCompareTo() {
        MetaPerformance mf1 = metaFactory.newMetaPerformance(p1);
        MetaPerformance mf2 = metaFactory.newMetaPerformance(p2);
        assertTrue(mf1.compareTo(mf2) > 0);
        assertTrue(mf1.compareTo(p2) > 0);
        assertTrue(mf2.compareTo(mf1) < 0);
        assertTrue(mf2.compareTo(p1) < 0);
        
        mf1 = metaFactory.newMetaPerformance(p2);
        assertTrue(mf1.compareTo(mf2) == 0);
        assertTrue(mf1.compareTo(p2) == 0);
    }
    
    @Test
    public void verifyGetters() {
        assertEquals(dateTime.toLocalDate(), metaFactory.newMetaPerformance(p1).getDate());
        assertEquals(dateTime.toLocalTime(), metaFactory.newMetaPerformance(p2).getTime());
    }
}
