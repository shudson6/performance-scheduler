package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaFeature;

public class FeatureValueListerTest {
    FeatureFactory ftrFactory = FeatureFactory.newFactory();
    UUID uuid = new UUID(0xabcd987612341234L, 0x1234abcdef567890L);
    LocalDateTime ldt = LocalDateTime.of(2020, 3, 27, 19, 15);

    @Test
    public void test() {
        Feature ftr = ftrFactory.createFeature("Foo", Rating.R, 90, false, true, true, true);
        MetaFeature mf = new TestMetaFeature(ftr, uuid, ldt, null);
        assertEquals("('abcd9876-1234-1234-1234-abcdef567890','Foo','R',90,false,true,true,true,"
                + "'2020-03-27 19:15:00','2020-03-27 19:15:00',true)", new FeatureValueLister().listValues(mf));
    }
    
    @Test
    public void nonexistentCol() {
        assertEquals("[null]", new FeatureValueLister().colValue("foobar", null));
    }
    
    @Test
    public void nullFeatureShouldGetEmptyString() {
        assertTrue(new FeatureValueLister().listValues(null).isEmpty());
    }
    
    @Test
    public void nullWrappedShouldGetEmptyString() {
        assertTrue(new FeatureValueLister().listValues(new TestMetaFeature(null, uuid, ldt, null)).isEmpty());
    }
    
    @Test
    public void columnOrder() {
        ArrayList<String> cols = new ArrayList<>(11);
        cols.add(SQL.COL_UUID);
        cols.add(SQL.COL_TITLE);
        cols.add(SQL.COL_RATING);
        cols.add(SQL.COL_RUNTIME);
        cols.add(SQL.COL_IS3D);
        cols.add(SQL.COL_CC);
        cols.add(SQL.COL_OC);
        cols.add(SQL.COL_DA);
        cols.add(SQL.COL_CREATED);
        cols.add(SQL.COL_CHANGED);
        cols.add(SQL.COL_ACTIVE);
        assertTrue(new FeatureValueLister().columnOrder().containsAll(cols));
    }
}
