package performancescheduler.data;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.time.LocalDateTime;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PerformanceTest {
    private static FeatureFactory ftrFactory;
    private static PerformanceFactory perfFactory;
    private static Matcher<String> matchPI;
    
    Feature ftr1, ftr2;
    Performance[] perf;
    Auditorium aud1, aud2;
    LocalDateTime ldt1, ldt2;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        ftrFactory = FeatureFactory.newFactory();
        perfFactory = PerformanceFactory.newFactory();
        matchPI = CoreMatchers.containsString("PerformanceImpl");
    }
    
    @Before
    public void setUp() {
        ftr1 = ftrFactory.createFeature("Foobar", Rating.NR, 90, false, false, false, false);
        ftr2 = ftrFactory.createFeature("Deez Nutz", Rating.PG, 69, true, false, false, false);
        aud1 = Auditorium.getInstance(1, null, false, 100);
        aud2 = Auditorium.getInstance(2, null, true, 75);
        ldt1 = LocalDateTime.of(2019, 12, 31, 19, 37);
        ldt2 = LocalDateTime.of(2020, 1, 1, 11, 47);
        
        perf = new Performance[] {
                perfFactory.createPerformance(ftr1, ldt1, 1, 0, 0, 0),
                perfFactory.createPerformance(ftr2, ldt2, 2, 0, 0, 0),
                perfFactory.createPerformance(ftr1, ldt1, 2, 0, 0, 0),
                perfFactory.createPerformance(ftr2, ldt1, 1, 0, 0, 0),
                perfFactory.createPerformance(ftr2, ldt2, 1, 0, 0, 0)
        };
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldAllowNullFeature() {
        assertEquals(new PerformanceImpl(null, ldt1, 1, 0, 0, 0), 
                perfFactory.createPerformance(null, ldt1, 1, 0, 0, 0));
    }
    
    @Test
    public void shouldThrowNullPointerExceptionNullDateTime() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(both(matchPI).and(containsString("date")));
        perfFactory.createPerformance(ftr1, null, 1, 0, 0, 0);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionAnyNegativeParameter() {
        for (int i = 1; i < 16; i++) {
            try {
                new PerformanceImpl(ftr1, ldt1, -1 * (i & 0x01), -1 * (i & 0x02), -1 * (i & 0x04), -1 * (i & 0x08));
                fail("Expected IllegalArgumentException.");
            } catch (IllegalArgumentException ex) {
                // this is what we expect; do nothing
            }
        }
    }

    @Test
    public void testGetters() {
        Performance p = perfFactory.createPerformance(ftr1, ldt1, 1, 17, 19, 23);
        assertEquals(ftr1, p.getFeature());
        assertEquals(ldt1, p.getDateTime());
        assertEquals(1, p.getAuditorium());
        assertEquals(ldt1.toLocalDate(), p.getDate());
        assertEquals(ldt1.toLocalTime(), p.getTime());
        assertEquals(17, p.getSeating());
        assertEquals(19, p.getCleanup());
        assertEquals(23, p.getTrailers());
        String str = String.format("%s | %s, %s | aud %d", ftr1.getTitle(), ldt1.toLocalDate().toString(),
                ldt1.toLocalTime().toString(), aud1.getNumber());
        assertEquals(str, p.toString());
    }
    
    @Test
    public void testToStringNullFeature() {
        assertEquals(String.format("[null feature] | %s, %s | aud 1", ldt1.toLocalDate().toString(),
                ldt1.toLocalTime().toString()), 
                new PerformanceImpl(null, ldt1, 1, 0, 0, 0).toString());
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testNotEquals() {
        for (int i = 0; i < perf.length - 1; i++) {
            for (int j = i + 1; j < perf.length; j++) {
                assertFalse(perf[i].equals(perf[j]));
                assertFalse(perf[j].equals(perf[i]));
            }
        }
        assertFalse(perf[0].equals(null));
        assertFalse(perf[0].equals("foo"));
    }
    
    @Test
    public void testEquals() {
        Performance pfm = perfFactory.createPerformance(ftr1, ldt1, 1, 0, 0, 0);
        assertTrue(pfm.equals(perf[0]));
        assertTrue(perf[0].equals(pfm));
        assertTrue(pfm.equals(pfm));
    }
    
    @Test
    public void testEqualsWithNullFeature() {
        Performance p1 = perfFactory.createPerformance(null, ldt1, 1, 0, 0, 0);
        assertFalse(p1.equals(perf[0]));
        assertFalse(perf[0].equals(p1));
        Performance p2 = perfFactory.createPerformance(null, ldt1, 1, 0, 0, 0);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }
    
    @Test
    public void testEqualsWithDifferentAttributes() {
        Performance ctrl = perfFactory.createPerformance(ftr1, ldt1, 0, 0, 0, 0);
        for (int i = 1; i < 8; i++) {
            assertNotEquals(ctrl, perfFactory.createPerformance(ftr1, ldt1, 0, i & 0x01, i & 0x02, i & 0x04));
        }
    }
    
    @Test
    public void testCompareToNotEqual() {
        // ftr1 > ftr2 so perf[0] > perf[1]
        assertTrue(perf[0].compareTo(perf[1]) > 0);
        assertTrue(perf[1].compareTo(perf[0]) < 0);
        // aud2 > aud1 so perf[1] > perf[3]
        assertTrue(perf[1].compareTo(perf[3]) > 0);
        assertTrue(perf[3].compareTo(perf[1]) < 0);
        // ldt2 > ldt1 so perf[4] > perf[3]
        assertTrue(perf[4].compareTo(perf[3]) > 0);
        assertTrue(perf[3].compareTo(perf[4]) < 0);
    }
    
    @Test
    public void testCompareToWithNullFeatures() {
        Performance p1 = perfFactory.createPerformance(null, ldt1, 1, 0, 0, 0);
        assertTrue(perf[0].compareTo(p1) > 0);
        assertTrue(p1.compareTo(perf[0]) < 0);
        Performance p2 = perfFactory.createPerformance(null, ldt2, 1, 0, 0, 0);
        assertTrue(p2.compareTo(p1) > 0);
        assertTrue(p1.compareTo(p2) < 0);
    }
    
    @Test
    public void testCompareToEqual() {
        // create a clone
        Performance pfm = perfFactory.createPerformance(ftr1, ldt1, 1, 0, 0, 0);
        // equal another object
        assertEquals(0, pfm.compareTo(perf[0]));
        assertEquals(0, perf[0].compareTo(pfm));
        // equal self
        assertEquals(0, pfm.compareTo(pfm));
    }
    
    @Test
    public void compareToShouldPutNullLast() {
        assertTrue(perf[0].compareTo(null) < 0);
    }
    
    @Test
    public void testHashCode() {
        for (Performance p : perf) {
            assertEquals(hash(p), p.hashCode());
        }
    }
    
    @Test
    public void testHashCodeWhenFeatureIsNull() {
        Performance p = perfFactory.createPerformance(null, ldt1, 1, 0, 0, 0);
        assertEquals(hash(p), p.hashCode());
    }
    
    private int hash(Performance p) {
        int rslt = 23;
        if (p.getFeature() != null) {
            rslt *= p.getFeature().hashCode();
        }
        rslt = 23 * rslt + p.getDateTime().hashCode();
        rslt = 23 * rslt + p.getAuditorium();
        rslt = 23 * rslt + p.getCleanup();
        rslt = 23 * rslt + p.getSeating();
        rslt = 23 * rslt + p.getTrailers();
        return rslt;
    }
}
