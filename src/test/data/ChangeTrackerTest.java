package test.data;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.data.ChangeTracker;
import main.data.Feature;
import main.data.FeatureImpl;
import main.data.Rating;

public class ChangeTrackerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void changeTrackerShouldThrowIllegalArgumentExceptionNullItem() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("ChangeTracker:"));
        new ChangeTracker<FeatureImpl>(null);
    }
    
    @Test
    public void traversalTest() {
        // these 3 identical instances should result in 
        // ChangeTrackers with different hashes due to the inclusion of nanoTime
        Feature f1 = FeatureImpl.getInstance("Foo", Rating.PG, 90, false, false, false, false);
        Feature f2 = FeatureImpl.getInstance("Foo", Rating.PG, 90, false, false, false, false);
        Feature f3 = FeatureImpl.getInstance("Foo", Rating.PG, 90, false, false, false, false);
        ChangeTracker<Feature> c1 = new ChangeTracker<>(f1);
        ChangeTracker<Feature> c2 = new ChangeTracker<>(f2);
        ChangeTracker<Feature> c3 = new ChangeTracker<>(f3);
        // find out
        assertFalse(Arrays.equals(c1.getHash(), c2.getHash()));
        assertFalse(Arrays.equals(c2.getHash(), c3.getHash()));
        assertFalse(Arrays.equals(c3.getHash(), c1.getHash()));
        // for funsies, let's see them
        System.out.format("f1: %s%nf2: %s%nf3: %s%n", c1.getHumanReadableHash(),
                c2.getHumanReadableHash(), c3.getHumanReadableHash());
        // now, use c1, f2, and f3 to create and traverse links
        assertEquals(0, c1.changeCount());
        c1.addChange(f3);
        c1.revert();
        assertFalse(c1.hasChanged());
        c1.revert();
        assertEquals(f1, c1.getCurrentState());
        c1.addChange(f2);
        c1.addChange(f3);
        assertEquals(f3, c1.getCurrentState());
        assertTrue(c1.hasChanged());
        c1.revert();
        assertEquals(f2, c1.getCurrentState());
        assertEquals(1, c1.changeCount());
    }
}
