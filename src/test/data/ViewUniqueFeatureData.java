package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data.Rating;
import main.data.UniqueFeature;

public class ViewUniqueFeatureData {

    @Test
    public void test() {
        UniqueFeature f = UniqueFeature.getInstance("Foo", Rating.R, 90);
        UniqueFeature g = UniqueFeature.getInstance("Bar", Rating.NC17, 87);
        assertEquals(32, f.getUniqueID().length);
        assertEquals(32, g.getUniqueID().length);
        System.out.format("%s: %s%n", f.getTitle(), new String(f.getUniqueID()));
        System.out.format("%s: %s%n", g.getTitle(), new String(g.getUniqueID()));
    }
}
