package performancescheduler.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.FeatureWrapper;
import performancescheduler.data.Rating;
import performancescheduler.util.UUIDGenerator;

public class FeatureWrapperTest {
    static class FW extends FeatureWrapper {
        FW(Feature ftr) {
            super(ftr);
        }
    }
    
    UUIDGenerator idGen;
    FeatureFactory ftrFactory;
    Feature ftr1;
    Feature ftr2;
    
    @Before
    public void setUp() {
        idGen = new UUIDGenerator();
        ftrFactory = FeatureFactory.newFactory();
        ftr1 = ftrFactory.createFeature("Foo", Rating.PG, 90, false, true, true, true);
        ftr2 = ftrFactory.createFeature("Bar", Rating.PG13, 100, false, true, true, true);
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void selfWrapShouldCauseIAE() {
        FeatureWrapper fw = new FeatureWrapper(ftr1);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("may not wrap itself");
        fw.setWrappedFeature(fw);
    }
    
    @Test
    public void recursiveWrapGrowingUpShouldCauseIAE() {
        // wrap a feature
        FeatureWrapper fw = new FeatureWrapper(ftr1);
        // wrap that FeatureWrapper
        FeatureWrapper top = new FeatureWrapper(fw);
        // wrap _that_ FeatureWrapper (we're 4 features deep--3 meta!)
        top = new FeatureWrapper(top);
        // changing the bottom to wrap the top should get thrown
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("may not wrap itself");
        fw.setWrappedFeature(top);
    }
    
    @Test
    public void wrappingNullShouldCauseNPE() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("feature must not");
        new FeatureWrapper(null);
    }
    
    @Test
    public void verifyAllGetters() {
        FeatureWrapper fw = new FeatureWrapper(ftr1);
        assertEquals(ftr1.getTitle(), fw.getTitle());
        assertEquals(ftr1.getRating(), fw.getRating());
        assertEquals(ftr1.getRuntime(), fw.getRuntime());
        assertEquals(ftr1.is3d(), fw.is3d());
        assertEquals(ftr1.hasClosedCaptions(), fw.hasClosedCaptions());
        assertEquals(ftr1.hasOpenCaptions(), fw.hasOpenCaptions());
        assertEquals(ftr1.hasDescriptiveAudio(), fw.hasDescriptiveAudio());
        assertEquals(ftr1.toString(), fw.toString());
        assertEquals(ftr1.hashCode(), fw.hashCode());
    }
    
    @Test
    public void verifyEquals() {
        FW fw1 = new FW(ftr1);
        FW fw2 = new FW(ftr1);
        assertTrue(fw1.equals(fw2));
        assertTrue(fw2.equals(fw1));
        assertTrue(fw1.equals(ftr1));
    }
    
    @Test
    public void verifyCompareTo() {
        FW fw1 = new FW(ftr1);
        FW fw2 = new FW(ftr2);
        assertTrue(fw1.compareTo(fw2) > 0);
        assertTrue(fw1.compareTo(ftr2) > 0);
        assertTrue(fw2.compareTo(fw1) < 0);
        assertTrue(fw2.compareTo(ftr1) < 0);
        
        fw1 = new FW(ftr2);
        assertTrue(fw1.compareTo(fw2) == 0);
        assertTrue(fw1.compareTo(ftr2) == 0);
    }
}