package test.java.data;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.data.Feature;
import main.java.data.FeatureImpl;
import main.java.data.Rating;

public class FeatureImplTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldThrowIllegalArgumentExceptionTitleNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(containsString("Title")).and(containsString("null")));
        FeatureImpl.getInstance(null, Rating.R, 90, false, false, false, false);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionTitleEmpty() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(containsString("Title")).and(containsString("empty")));
        FeatureImpl.getInstance("", Rating.R, 90, false, false, false, false);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionRatingNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(containsString("Rating")).and(containsString("null")));
        FeatureImpl.getInstance("Foo", null, 90, false, false, false, false);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionRuntimeNegative() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("runtime"));
        FeatureImpl.getInstance("Foo", Rating.R, -20, false, false, false, false);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionRuntime0() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("runtime"));
        FeatureImpl.getInstance("Foo", Rating.R, 0, false, false, false, false);
    }
    
    @Test
    public void checkAll16combinationsOfAmenitiesViaGetters() {
        for (int i = 0; i < 16; i++) {
            boolean _3d = (i & 0x08) != 0;
            boolean cc = (i & 0x04) != 0;
            boolean oc = (i & 0x02) != 0;
            boolean da = (i & 0x01) != 0;
            Feature f = FeatureImpl.getInstance("Foo", Rating.G, i + 90, _3d, cc, oc, da);
            assertEquals(_3d, f.is3d());
            assertEquals(cc, f.hasClosedCaptions());
            assertEquals(oc, f.hasOpenCaptions());
            assertEquals(da, f.hasDescriptiveAudio());
            assertEquals("Foo", f.getTitle());
            assertEquals(Rating.G, f.getRating());
            assertEquals(i + 90, f.getRuntime());
            // print it (cover toString() as well)
            System.out.println(f.toString());
        }
    }
}
