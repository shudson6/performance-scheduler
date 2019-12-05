package performancescheduler.data.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.DbConnection;
import performancescheduler.entity.FeatureManager;
import performancescheduler.entity.PerformanceManager;

public class DbConnectionTest {
    private static FeatureFactory ftrFactory;
    private static PerformanceFactory perfFactory;
    private static DbConnection db;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        db = DbConnection.getDatabase();
        ftrFactory = FeatureFactory.newFactory();
        perfFactory = new PerformanceFactory();
    }

    @Test
    public void saveFeaturesTest() {
        FeatureManager mgr = new FeatureManager();
        mgr.addItem(ftrFactory.createFeature("Hello", Rating.G, 90, false, false, false, false));
        mgr.addItem(ftrFactory.createFeature("Nognog", Rating.NR, 105, false, true, false, true));
        assertTrue(db.saveFeatureData(mgr));
    }
    
    @Test
    public void loadFeaturesTest() {
        FeatureManager mgr = new FeatureManager();
        db.loadFeatureData(mgr);
        mgr.getAll().forEach(ftr -> System.out.println(ftr.toString()));
    }

    @Test
    public void savePerformanceTest() {
        FeatureManager ftrMgr = new FeatureManager();
        db.loadFeatureData(ftrMgr);
        if (ftrMgr.isEmpty()) {
            fail("No features available.");
        }
        PerformanceManager perfMgr = new PerformanceManager();
        Feature [] ftrs = ftrMgr.getAll().toArray(new Feature[ftrMgr.getAll().size()]);
        Auditorium aud = Auditorium.getInstance(2, null, false, 78);
        perfMgr.addItem(perfFactory.createPerformance(ftrs[0], LocalDateTime.now(), aud));
        assertTrue(db.savePerformanceData(perfMgr, ftrMgr));
    }
}
