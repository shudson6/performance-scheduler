package test.data;

import static org.junit.Assert.*;

import org.junit.Test;

import main.data.Rating;
import main.data.UniqueFeature;

public class ViewUniqueFeatureData {

    @Test
    public void test() {
        UniqueFeature f = UniqueFeature.getInstance("Foo", Rating.R, 90, false, false, false, false);
        UniqueFeature g = UniqueFeature.getInstance("Bar", Rating.NC17, 87, false, false, false, false);
        assertEquals(32, f.getHash().length);
        assertEquals(32, g.getHash().length);
        System.out.format("%s: %s%n", f.getTitle(), new String(f.getHumanReadableHash()));
        System.out.format("%s: %s%n", g.getTitle(), new String(g.getHumanReadableHash()));
    }
}
