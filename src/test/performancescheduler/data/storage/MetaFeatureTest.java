package performancescheduler.data.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Test;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.util.UUIDGenerator;

public class MetaFeatureTest {
    UUIDGenerator idGenerator;
    FeatureFactory ftrFactory;
    MetaDataFactory metaFactory;
    Feature ftr1;
    Feature ftr2;
    LocalDateTime dateTime;

    @Before
    public void setUp() {
        idGenerator = new UUIDGenerator();
        ftrFactory = FeatureFactory.newFactory();
        metaFactory = MetaDataFactory.newFactory();
        ftr1 = ftrFactory.createFeature("Foo", Rating.R, 90, false, false, false, false);
        ftr2 = ftrFactory.createFeature("Bar", Rating.R, 90, false, false, false, false);
        dateTime = LocalDateTime.now();
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void nullIdShouldCauseNPE() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("id");
        new MetaFeature(ftr1, null, null, null);
    }
    
    @Test
    public void nullCreationTimeShouldCauseNPE() {
        exception.expect(NullPointerException.class);
        exception.expectMessage("creation");
        new MetaFeature(ftr1, idGenerator.generateUUID(), null, null);
    }
    
    @Test
    public void nullChangedTimeShouldResultInMatchingCreationTime() {
        MetaFeature mf = new MetaFeature(ftr1, idGenerator.generateUUID(), dateTime, null);
        assertEquals(mf.getCreatedTimestamp(), mf.getChangedTimestamp());
    }

    @Test
    public void checkAllMembersSetCorrectly() {
        UUID id = idGenerator.generateUUID();
        MetaFeature mf = new MetaFeature(ftr1, id, dateTime, dateTime.plusHours(2));
        assertEquals(ftr1.getTitle(), mf.getTitle());
        assertEquals(id, mf.getUuid());
        assertEquals(dateTime, mf.getCreatedTimestamp());
        assertEquals(dateTime.plusHours(2), mf.getChangedTimestamp());
    }
    
    @Test
    public void checkToString() {
        assertEquals(ftr1.toString(), metaFactory.newMetaFeature(ftr1).toString());
    }
    
    @Test
    public void checkHashCode() {
        UUID id = idGenerator.generateUUID();
        assertEquals(id.hashCode(), new MetaFeature(ftr1, id, dateTime, null).hashCode());
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void checkEquals() {
        UUID id = idGenerator.generateUUID();
        MetaFeature mfControl = new MetaFeature(ftr1, id, dateTime, null);
        // 16 paths in equals(), using counter to control which variables are set so all paths are taken
        for (int i = 0; i < 16; i++) {
            MetaFeature mf = new MetaFeature((i & 0x08) > 0 ? ftr1 : ftr2,
                    (i & 0x04) > 0 ? id : idGenerator.generateUUID(),
                    (i & 0x02) > 0 ? dateTime : dateTime.plusHours(1),
                    (i & 0x01) > 0 ? dateTime : dateTime.plusHours(2));
            if (i == 15) {
                assertTrue(mf.equals(mfControl));
                assertTrue(mfControl.equals(mf));
            } else {
                assertFalse(mf.equals(mfControl));
                assertFalse(mfControl.equals(mf));
            }
        }
        assertFalse(mfControl.equals("foo"));
    }
    
    @Test
    public void verifyCompareTo() {
        MetaFeature mf1 = metaFactory.newMetaFeature(ftr1);
        MetaFeature mf2 = metaFactory.newMetaFeature(ftr2);
        assertTrue(mf1.compareTo(mf2) > 0);
        assertTrue(mf1.compareTo(ftr2) > 0);
        assertTrue(mf2.compareTo(mf1) < 0);
        assertTrue(mf2.compareTo(ftr1) < 0);
        
        mf1 = metaFactory.newMetaFeature(ftr2);
        assertTrue(mf1.compareTo(mf2) == 0);
        assertTrue(mf1.compareTo(ftr2) == 0);
    }
}
