package test.java.data;

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

import main.java.data.Auditorium;
import main.java.data.Feature;
import main.java.data.FeatureFactory;
import main.java.data.Performance;
import main.java.data.PerformanceFactory;
import main.java.data.Rating;

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
        ftrFactory = new FeatureFactory();
        perfFactory = new PerformanceFactory();
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
                perfFactory.createPerformance(ftr1, ldt1, aud1),
                perfFactory.createPerformance(ftr2, ldt2, aud2),
                perfFactory.createPerformance(ftr1, ldt1, aud2),
                perfFactory.createPerformance(ftr2, ldt1, aud1),
                perfFactory.createPerformance(ftr2, ldt2, aud1)
        };
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNullFeature() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(matchPI).and(containsString("feature")));
        perfFactory.createPerformance(null, ldt1, aud1);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNullDateTime() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(matchPI).and(containsString("date")));
        perfFactory.createPerformance(ftr1, null, aud1);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNoLocation() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(matchPI).and(containsString("location")));
        perfFactory.createPerformance(ftr1, ldt1, null);
    }

    @Test
    public void testGetters() {
        Performance p = perfFactory.createPerformance(ftr1, ldt1, aud1);
        assertEquals(ftr1, p.getFeature());
        assertEquals(ldt1, p.getDateTime());
        assertEquals(aud1, p.getAuditorium());
        assertEquals(ldt1.toLocalDate(), p.getDate());
        assertEquals(ldt1.toLocalTime(), p.getTime());
        String str = String.format("%s | %s, %s | %s", ftr1.getTitle(), ldt1.toLocalDate().toString(),
                ldt1.toLocalTime().toString(), aud1.getName());
        assertEquals(str, p.toString());
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
        Performance pfm = perfFactory.createPerformance(ftr1, ldt1, aud1);
        assertTrue(pfm.equals(perf[0]));
        assertTrue(perf[0].equals(pfm));
        assertTrue(pfm.equals(pfm));
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
    public void testCompareToEqual() {
        // create a clone
        Performance pfm = perfFactory.createPerformance(ftr1, ldt1, aud1);
        // equal another object
        assertEquals(0, pfm.compareTo(perf[0]));
        assertEquals(0, perf[0].compareTo(pfm));
        // equal self
        assertEquals(0, pfm.compareTo(pfm));
    }
    
    @Test
    public void compareToShouldThrowNPE() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(matchPI);
        perf[0].compareTo(null);
    }
}
