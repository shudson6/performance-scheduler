package performancescheduler.data.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.UuidableFeature;
import performancescheduler.util.UUIDGenerator;

public class UuidableFeatureTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test() {
        FeatureFactory factory = FeatureFactory.newFactory();
        Feature ftr = factory.createFeature("foo", Rating.G, 100, false, false, false, false);
        UuidableFeature uftr = new UuidableFeature(ftr, new UUIDGenerator().generateUUID());
        exception.expect(IllegalArgumentException.class);
        uftr.setWrappedFeature(uftr);
    }
}
