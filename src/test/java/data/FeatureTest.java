package test.java.data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.data.Feature;
import main.java.data.FeatureFactory;
import main.java.data.Rating;

public class FeatureTest {
    private static FeatureFactory ftrFactory;
    
    private static ArrayList<Feature> features;
    
    @BeforeClass
    public static void setUpBefore() {
        ftrFactory = new FeatureFactory();
        features = create16ftrs();
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldThrowIllegalArgumentExceptionTitleNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(containsString("Title")).and(containsString("null")));
        ftrFactory.createFeature(null, Rating.R, 90, false, false, false, false);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionTitleEmpty() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(containsString("Title")).and(containsString("empty")));
        ftrFactory.createFeature("", Rating.R, 90, false, false, false, false);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionRatingNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(containsString("Rating")).and(containsString("null")));
        ftrFactory.createFeature("Foo", null, 90, false, false, false, false);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionRuntimeNegative() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("runtime"));
        ftrFactory.createFeature("Foo", Rating.R, -20, false, false, false, false);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionRuntime0() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("runtime"));
        ftrFactory.createFeature("Foo", Rating.R, 0, false, false, false, false);
    }
    
    @Test
    public void testNotEqualsOnBooleans() {
        for (int i = 0; i < features.size(); i++) {
            for (int j = i + 1; j < features.size(); j++) {
                assertFalse(features.get(i).equals(features.get(j)));
                assertFalse(features.get(j).equals(features.get(i)));
            }
        }
    }
    
    @Test
    public void testEquals() {
        Feature ftr = ftrFactory.createFeature("Foo", Rating.NR, 90, false, false, false, false);
        assertTrue(ftr.equals(features.get(0)));
        assertTrue(features.get(0).equals(ftr));
    }
    
    @Test
    public void testNotEqualsNotCoveredByBooleanTester() {
        Feature ftr = ftrFactory.createFeature("Bar", Rating.NR, 90, false, false, false, false);
        // other method doesn't cover cases where titles don't match
        assertFalse(ftr.equals(features.get(0)));
        assertFalse(features.get(0).equals(ftr));
        assertFalse(ftr.equals(features.get(8)));
        assertFalse(features.get(0).equals(ftr));
        // nor ratings
        ftr = ftrFactory.createFeature("Foo", Rating.PG13, 90, false, false, false, false);
        assertFalse(ftr.equals(features.get(0)));
        assertFalse(features.get(0).equals(ftr));
        // nor runtime
        ftr = ftrFactory.createFeature("Foo", Rating.NR, 101, false, false, false, false);
        assertFalse(ftr.equals(features.get(0)));
        assertFalse(features.get(0).equals(ftr));
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEqualsBadInput() {
        assertFalse(features.get(0).equals(null));
        assertFalse(features.get(0).equals("foo"));
    }
    
    @Test
    public void compareToShouldThrowNPE() {
        exception.expect(NullPointerException.class);
        exception.expectMessage(CoreMatchers.containsString("compareTo"));
        features.get(0).compareTo(null);
    }
    
    @Test
    public void testCompareToBranchCoverage() {
        // titles not equal
        Feature ftr1 = ftrFactory.createFeature("Foo", Rating.R, 77, false, false, false, false);
        Feature ftr2 = ftrFactory.createFeature("Bar", Rating.R, 77, false, false, false, false);
        assertTrue(ftr1.compareTo(ftr2) > 0);
        assertTrue(ftr2.compareTo(ftr1) < 0);
        // same title, different 3d
        assertTrue(features.get(7).compareTo(features.get(15)) < 0);
        assertTrue(features.get(15).compareTo(features.get(7)) > 0);
        // same title & 3d; different cc
        assertTrue(features.get(11).compareTo(features.get(15)) < 0);
        assertTrue(features.get(15).compareTo(features.get(11)) > 0);
        // same title, 3d, cc; not oc
        assertTrue(features.get(5).compareTo(features.get(7)) < 0);
        assertTrue(features.get(7).compareTo(features.get(5)) > 0);
        // same thru to da
        assertTrue(features.get(0).compareTo(features.get(1)) < 0);
        assertTrue(features.get(1).compareTo(features.get(0)) > 0);
        // differ only on rating
        assertTrue(features.get(0).compareTo(ftr1) < 0);
        assertTrue(ftr1.compareTo(features.get(0)) > 0);
        // differ only on runtime
        ftr2 = ftrFactory.createFeature("Foo", Rating.R, 100, false, false, false, false);
        assertTrue(ftr1.compareTo(ftr2) < 0);
        assertTrue(ftr2.compareTo(ftr1) > 0);
    }
    
    @Test
    public void testToString() {
        assertEquals("Foo 90min NR", features.get(0).toString());
        assertEquals("Foo 3D 90min NR", features.get(8).toString());
        assertEquals("Foo 90min NR CC OC", features.get(6).toString());
        assertEquals("Foo 90min NR OC DA", features.get(3).toString());
        Feature ftr = ftrFactory.createFeature("Foo 3D", Rating.PG, 77, true, true, false, false);
        assertEquals("Foo 3D 77min PG CC", ftr.toString());
    }
    
/*    0       4       8      12
 *  0000    0100    1000    1100
 *  0001    0101    1001    1101
 *  0010    0110    1010    1110
 *  0011    0111    1011    1111
 */
    // sets up an ArrayList of 16 Features, covering all combos of amenities
    private static ArrayList<Feature> create16ftrs() {
        ArrayList<Feature> ftrs = new ArrayList<>(16);
        for (int i = 0; i < 16; i++) {
            ftrs.add(ftrFactory.createFeature("Foo", Rating.NR, 90, (i & 0x08) != 0, (i & 0x04) != 0,
                    (i & 0x02) != 0, (i & 0x01) != 0));
        }
        return ftrs;
    }
}
