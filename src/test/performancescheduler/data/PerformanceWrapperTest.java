package performancescheduler.data;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class PerformanceWrapperTest {
    static class PW extends PerformanceWrapper {
        PW(Performance p) {
            super(p);
        }
    }
    
    FeatureFactory ftrFactory;
    PerformanceFactory perfFactory;
    LocalDateTime ldt;
    Auditorium aud;
    
    @Before
    public void setUp() {
        ftrFactory = FeatureFactory.newFactory();
        perfFactory = PerformanceFactory.newFactory();
        ldt = LocalDateTime.now();
        aud = Auditorium.getInstance(1, "Foobar", false, 100);
    }
    
    @Test
    public void verifyGetters() {
        Feature f = ftrFactory.createFeature("Foo", Rating.G, 90, false, false, false, false);
        Performance p = perfFactory.createPerformance(f, ldt, aud);
        PW pw = new PW(p);
        assertEquals(p.getFeature(), pw.getFeature());
        assertEquals(p.getDate(), pw.getDate());
        assertEquals(p.getDateTime(), pw.getDateTime());
        assertEquals(p.getAuditorium(), pw.getAuditorium());
        assertEquals(p.getTime(), pw.getTime());
    }

    @Test
    public void verifyCompareTo() {
        Feature f1 = ftrFactory.createFeature("Foo", Rating.NR, 90, false, false, false, false);
        Feature f2 = ftrFactory.createFeature("Bar", Rating.NR, 90, false, false, false, false);
        Performance p1 = perfFactory.createPerformance(f1, ldt, aud);
        Performance p2 = perfFactory.createPerformance(f2, ldt, aud);
        PW pw1 = new PW(p1);
        PW pw2 = new PW(p2);
        
        assertTrue(pw1.compareTo(pw2) > 0);
        assertTrue(pw2.compareTo(pw1) < 0);
        assertTrue(pw1.compareTo(p2) > 0);
        assertTrue(pw2.compareTo(p1) < 0);
        
        pw2.setWrapped(perfFactory.createPerformance(f1, ldt, aud));
        assertEquals(0, pw1.compareTo(pw2));
        assertEquals(0, pw2.compareTo(pw1));
    }
}
