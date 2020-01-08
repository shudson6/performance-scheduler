package performancescheduler;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaDataFactory;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public final class TestData {
    public static final String TEST_TBL_FEATURE = "testfeatures";
    public static final String TEST_TBL_PERFORMANCE = "testperformances";
    
    public static final FeatureFactory featureFactory = FeatureFactory.newFactory();
    public static final PerformanceFactory performanceFactory = PerformanceFactory.newFactory();
    public static final MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    
    public static final UUID uuid0 = UUID.fromString("0d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid1 = UUID.fromString("1d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid2 = UUID.fromString("2d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid3 = UUID.fromString("3d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid4 = UUID.fromString("4d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid5 = UUID.fromString("5d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid6 = UUID.fromString("6d725a2e-dead-beef-9f37-82d33246a8c3");
    public static final UUID uuid7 = UUID.fromString("7d725a2e-dead-beef-9f37-82d33246a8c3");

    public static final LocalDateTime ldtTrinity = LocalDateTime.of(1945, 6, 16, 5, 29);  // first nuclear test
    public static final LocalDateTime ldtBravo = LocalDateTime.of(1954, 3, 1, 6, 45);   // castle bravo
    public static final LocalDateTime ldtJulin = LocalDateTime.of(1992, 9, 23, 15, 4);   // last nuclear test (USA)
    
    public static final Auditorium aud1 = Auditorium.getInstance(1, null, false, 100);
    public static final Auditorium aud2 = Auditorium.getInstance(2, null, true, 100);
    
    public static final Feature ftrFoo = featureFactory.createFeature("Foo", Rating.PG, 90, false, false, false, false);
    public static final Feature ftrBar = featureFactory.createFeature("Bar", Rating.R, 99, false, true, true, true);
    public static final MetaFeature mfFoo = metaFactory.newMetaFeature(ftrFoo, uuid0, ldtTrinity, null);
    public static final MetaFeature mfBar = metaFactory.newMetaFeature(ftrBar, uuid1, ldtTrinity, null);
    
    public static final Performance pfmFoo1 = performanceFactory
            .createPerformance(mfFoo, ldtJulin, aud1.getNumber(), 0, 0, 0);
    public static final Performance pfmBar2 = performanceFactory
            .createPerformance(mfBar, ldtJulin, aud2.getNumber(), 0, 0, 0);
    public static final MetaPerformance mpFoo1 = metaFactory.newMetaPerformance(pfmFoo1, uuid2, ldtTrinity, null);
    public static final MetaPerformance mpBar2 = metaFactory.newMetaPerformance(pfmBar2, uuid3, ldtTrinity, null);
    
    public static Properties PROPERTIES() {
        Properties p = new Properties();
        p.put("features", TEST_TBL_FEATURE);
        p.put("performances", TEST_TBL_PERFORMANCE);
        return p;
    }
    
    private TestData() {}
}
