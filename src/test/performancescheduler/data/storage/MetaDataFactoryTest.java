package performancescheduler.data.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;

public class MetaDataFactoryTest {
    MetaDataFactory fac = MetaDataFactory.newFactory();
    FeatureFactory fF = FeatureFactory.newFactory();
    PerformanceFactory pF = PerformanceFactory.newFactory();

    @Test
    public void copiedFeatureHasEqualMembers() {
        MetaFeature mf = fac.newMetaFeature(fF.createFeature("Foobar", Rating.R, 90, false, false, false, false));
        MetaFeature mc = fac.copyMetaData(mf, mf);
        assertEquals(mf.getUuid(), mc.getUuid());
        assertEquals(mf.getCreatedTimestamp(), mc.getCreatedTimestamp());
        // don't check the "changed" timestamp; the whole point of the copy is we might change something and
        // the contract states it will not be the same
        assertEquals(mf.getTitle(), mc.getTitle());
    }
    
    @Test
    public void copiedPerformanceHasEqualMembers() {
        Feature f = fF.createFeature("Foobar", Rating.R, 90, false, true, false, false);
        Performance p = pF.createPerformance(f, LocalDateTime.of(2020, 10, 11, 12, 0),
                Auditorium.getInstance(1, null, false, 50));
        MetaPerformance mp = fac.newMetaPerformance(p);
        MetaPerformance mc = fac.copyMetaData(mp, p);
        assertEquals(mp.getUuid(), mc.getUuid());
        assertEquals(mp.getCreatedTimestamp(), mc.getCreatedTimestamp());
        // don't check the "changed" timestamp; the whole point of the copy is we might change something and
        // the contract states it will not be the same
        assertEquals(mp.getFeature(), mc.getFeature());
    }

}
