package test.main.data;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.data.Feature;
import main.data.FeatureImpl;
import main.data.Rating;

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
    
    /**
     * This test runs through all 16 possible combinations of cc, oc, da, and 3d,
     * to cover all paths in the toByteBuffer()
     * method. It also covers all getters.
     */
    @Test
    public void all16hashCodesMustDiffer_toByteBufferTest() {
        FeatureImpl ftr[] = new FeatureImpl[16];
        ftr[ 0] = FeatureImpl.getInstance("Test", Rating.G, 90, false, false, false, false);
        ftr[ 1] = FeatureImpl.getInstance("Test", Rating.G, 90, false, false, false,  true);
        ftr[ 2] = FeatureImpl.getInstance("Test", Rating.G, 90, false, false,  true, false);
        ftr[ 3] = FeatureImpl.getInstance("Test", Rating.G, 90, false, false,  true,  true);
        ftr[ 4] = FeatureImpl.getInstance("Test", Rating.G, 90, false,  true, false, false);
        ftr[ 5] = FeatureImpl.getInstance("Test", Rating.G, 90, false,  true, false,  true);
        ftr[ 6] = FeatureImpl.getInstance("Test", Rating.G, 90, false,  true,  true, false);
        ftr[ 7] = FeatureImpl.getInstance("Test", Rating.G, 90, false,  true,  true,  true);
        ftr[ 8] = FeatureImpl.getInstance("Test", Rating.G, 90,  true, false, false, false);
        ftr[ 9] = FeatureImpl.getInstance("Test", Rating.G, 90,  true, false, false,  true);
        ftr[10] = FeatureImpl.getInstance("Test", Rating.G, 90,  true, false,  true, false);
        ftr[11] = FeatureImpl.getInstance("Test", Rating.G, 90,  true, false,  true,  true);
        ftr[12] = FeatureImpl.getInstance("Test", Rating.G, 90,  true,  true, false, false);
        ftr[13] = FeatureImpl.getInstance("Test", Rating.G, 90,  true,  true, false,  true);
        ftr[14] = FeatureImpl.getInstance("Test", Rating.G, 90,  true,  true,  true, false);
        ftr[15] = FeatureImpl.getInstance("Test", Rating.G, 90,  true,  true,  true,  true);
        // generate all byte arrays
        // ftrBytes: from FeatureImpl.toByteBuffer()
        // tstBytes: from byteArray()
        byte ftrBytes[][] = new byte[ftr.length][];
        byte tstBytes[][] = new byte[ftr.length][];
        for (int i = 0; i < ftr.length; i++) {
            ftrBytes[i] = ftr[i].toByteBuffer().array();
            tstBytes[i] = byteArray(ftr[i]);
        }
        // compare all byte arrays in ftrBytes against all others
        // compare each in ftrBytes with its sister in tstBytes
        for (int i = 0; i < ftr.length; i ++) {
            assertArrayEquals(tstBytes[i], ftrBytes[i]);
            for (int j = i + 1; j < ftr.length; j++) {
                assertFalse(Arrays.equals(ftrBytes[i], ftrBytes[j]));
            }
        }
        // try to print them all
        for (byte[] b : ftrBytes) {
            for (byte i : b) {
                System.out.format("%x", i);
            }
            System.out.println();
        }
    }
    
    private byte[] byteArray(Feature f) {
        byte[] _t = f.getTitle().getBytes();
        byte[] _r = f.getRating().toString().getBytes();
        ByteBuffer buf = ByteBuffer.allocate(_t.length + _r.length + Integer.BYTES + 2 * Byte.BYTES);
        buf.put(_t);
        buf.put(_r);
        buf.putInt(f.getRuntime());
        buf.put((byte)(f.is3D() ? 1 : 0));
        // create byte for amenities
        byte am = (byte)((f.hasClosedCaptions() ? 1 : 0) + (f.hasOpenCaptions() ? 2 : 0) + (f.hasDescriptiveAudio() ? 4 : 0));
        buf.put(am);
        return buf.array();
    }
}
