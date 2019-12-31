package performancescheduler.data.storage.sql;

import java.time.LocalDateTime;
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

class TestData {
    static final String TEST_TBL_FEATURE = "testfeatures";
    static final String TEST_TBL_PERFORMANCE = "testperformances";
    
    static final FeatureFactory featureFactory = FeatureFactory.newFactory();
    static final PerformanceFactory performanceFactory = PerformanceFactory.newFactory();
    static final MetaDataFactory metaFactory = MetaDataFactory.newFactory();
    
    static final UUID uuid0 = UUID.fromString("0d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid1 = UUID.fromString("1d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid2 = UUID.fromString("2d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid3 = UUID.fromString("3d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid4 = UUID.fromString("4d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid5 = UUID.fromString("5d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid6 = UUID.fromString("6d725a2e-dead-beef-9f37-82d33246a8c3");
    static final UUID uuid7 = UUID.fromString("7d725a2e-dead-beef-9f37-82d33246a8c3");

    static final LocalDateTime ldtCreate = LocalDateTime.of(1945, 6, 16, 5, 29);  // first nuclear test
    static final LocalDateTime ldtChange = LocalDateTime.of(1954, 3, 1, 6, 45);   // castle bravo
    static final LocalDateTime ldtStart = LocalDateTime.of(1992, 9, 23, 15, 4);   // last nuclear test (USA)
    
    static final Auditorium aud1 = Auditorium.getInstance(1, null, false, 100);
    static final Auditorium aud2 = Auditorium.getInstance(2, null, true, 100);
    
    static final Feature ftrFoo = featureFactory.createFeature("Foo", Rating.PG, 90, false, false, false, false);
    static final Feature ftrBar = featureFactory.createFeature("Bar", Rating.R, 99, false, true, true, true);
    static final MetaFeature mfFoo = metaFactory.newMetaFeature(ftrFoo, uuid0, ldtCreate, null);
    static final MetaFeature mfBar = metaFactory.newMetaFeature(ftrBar, uuid1, ldtCreate, null);
    
    static final Performance pfmFoo1 = performanceFactory.createPerformance(ftrFoo, ldtStart, aud1);
    static final Performance pfmBar2 = performanceFactory.createPerformance(ftrBar, ldtStart, aud2);
    static final MetaPerformance mpFoo1 = metaFactory.newMetaPerformance(pfmFoo1, uuid2, ldtCreate, null);
    static final MetaPerformance mpBar2 = metaFactory.newMetaPerformance(pfmBar2, uuid3, ldtCreate, null);
}
