package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.TestData;
import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;
import performancescheduler.data.storage.TestMetaFeature;
import performancescheduler.data.storage.TestMetaPerformance;
import performancescheduler.util.UUIDGenerator;

public class SqlSaverTest {
    static final LocalDateTime testCreated = LocalDateTime.of(1945, 6, 16, 5, 29);  // first nuclear test
    static final LocalDateTime testChanged = LocalDateTime.of(1954, 3, 1, 6, 45);     // castle bravo
    static final LocalDateTime testGoTime = LocalDateTime.of(1992, 9, 23, 15, 4);  // last nuclear test (USA)
    
    static final String QUERY_COUNT = "SELECT COUNT(uuid) FROM %s WHERE created='1945-06-16 05:29:00';";
    static final String COUNT_DELETED = "SELECT COUNT(uuid) FROM %s WHERE active=false";
    static final String DELETE = "DELETE FROM %s WHERE created='1945-06-16 05:29:00';";
    
    static DbConnectionService dcs;
    static SqlSaver saver;
    
    static UUIDGenerator uuidGen = new UUIDGenerator();
    static FeatureFactory ftrFactory = FeatureFactory.newFactory();
    static PerformanceFactory pfmFactory = PerformanceFactory.newFactory();
    static Auditorium aud1 = Auditorium.getInstance(1, null, false, 100);
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @BeforeClass
    public static void setUpBefore() throws ClassNotFoundException, SQLException {
        dcs = new DbConnectionService(TestData.PROPERTIES());
        dcs.start();
        saver = new SqlSaver(dcs);
    }
    
    Feature ftrFoobar, ftrFoobar2;
    MetaFeature mfFoobar, mfFoobar2;
    Performance pfmFoobar, pfmFoobar2;
    MetaPerformance mpFoobar, mpFoobar2;
    
    @Before
    public void setUp() {
        ftrFoobar = ftrFactory.createFeature("Foobar", Rating.R, 100, false, false, false, false);
        ftrFoobar2 = ftrFactory.createFeature("Foobar", Rating.R, 101, false, true, true, true);
        mfFoobar = new TestMetaFeature(ftrFoobar, uuidGen.generateUUID(), testCreated, null);
        mfFoobar2 = new TestMetaFeature(ftrFoobar2, mfFoobar.getUuid(), testCreated, testChanged);
        pfmFoobar = pfmFactory.createPerformance(mfFoobar, testGoTime, aud1.getNumber(), 0, 0, 0);
        pfmFoobar2 = pfmFactory.createPerformance(mfFoobar2, testGoTime, aud1.getNumber(), 0, 0, 0);
        mpFoobar = new TestMetaPerformance(pfmFoobar, uuidGen.generateUUID(), testCreated, null);
        mpFoobar2 = new TestMetaPerformance(pfmFoobar2, mpFoobar.getUuid(), testCreated, null);
    }
    
    @After
    public void tearDown() throws SQLException, IOException {
        dcs.getStatement().execute(String.format(DELETE, TestData.TEST_TBL_PERFORMANCE));
        dcs.getStatement().execute(String.format(DELETE, TestData.TEST_TBL_FEATURE));
    }

    @Test
    public void saveFeature() throws IOException, SQLException {
        saver.save(Arrays.asList(mfFoobar), null);
        assertEquals(1, selectCount(TestData.TEST_TBL_FEATURE));
    }
    
    @Test
    public void shouldUpdateFeatureWithoutError() throws IOException, SQLException {
        saver.save(Arrays.asList(mfFoobar), null);
        saver.save(Arrays.asList(mfFoobar2), null);
        assertEquals(1, selectCount(TestData.TEST_TBL_FEATURE));
    }
    
    @Test
    public void shouldNotSavePerformanceWithoutCorrespondingFeature() {
        try {
            saver.save(null, Arrays.asList(mpFoobar));
        } catch (IOException ex) {
            assertTrue(ex.getCause().getMessage().contains("foreign key"));
            return;
        }
        fail("Didn't get expected exception!");
    }
    
    @Test
    public void shouldSaveFeatureAndPerformance() throws IOException, SQLException {
        saver.save(Arrays.asList(mfFoobar), Arrays.asList(mpFoobar));
        assertEquals(1, selectCount(TestData.TEST_TBL_FEATURE));
        assertEquals(1, selectCount(TestData.TEST_TBL_PERFORMANCE));
    }
    
    @Test
    public void featureAndPerformanceShouldBeRemoved() throws IOException, SQLException {
        saver.save(Arrays.asList(mfFoobar), Arrays.asList(mpFoobar));
        assertEquals(1, selectCount(TestData.TEST_TBL_FEATURE));
        assertEquals(1, selectCount(TestData.TEST_TBL_PERFORMANCE));
        MetaFeature mfDelete = new TestMetaFeature(null, mfFoobar.getUuid(), mfFoobar.getCreatedTimestamp(),
                testChanged);
        MetaPerformance mpDelete = new TestMetaPerformance(null, mpFoobar.getUuid(), mpFoobar.getCreatedTimestamp(),
                testChanged);
        saver.save(Arrays.asList(mfDelete), Arrays.asList(mpDelete));
        assertEquals(1, selectDeleted(TestData.TEST_TBL_FEATURE));
        assertEquals(1, selectDeleted(TestData.TEST_TBL_PERFORMANCE));
    }

    private int selectCount(String tbl) throws SQLException, IOException {
        ResultSet rs = dcs.getStatement().executeQuery(String.format(QUERY_COUNT, tbl));
        rs.next();
        return rs.getInt(1);
    }
    
    private int selectDeleted(String tbl) throws SQLException, IOException {
        ResultSet rs = dcs.getStatement().executeQuery(String.format(COUNT_DELETED, tbl));
        rs.next();
        return rs.getInt(1);
    }
}
