package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlLoaderTest {
    static DbConnectionService dbcs;

    @BeforeClass
    public static void before() throws ClassNotFoundException, IOException, SQLException {
        SqlTestUtil.insertTestData();
        dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
    }
    
    @AfterClass
    public static void after() throws ClassNotFoundException, SQLException, IOException {
        SqlTestUtil.removeTestData();
        dbcs.close();
    }
    
    @Test
    public void test() throws ClassNotFoundException, SQLException, IOException {
        Collection<MetaFeature> ftrs = new ArrayList<>();
        Collection<MetaPerformance> pfms = new ArrayList<>();
        new SqlLoader(dbcs).load(ftrs, pfms, TestData.ldtCreate.toLocalDate(), LocalDate.now());
        assertEquals(2, ftrs.size());
        assertEquals(2, pfms.size());
    }
    
    @Test
    public void testOutOfOrderDateParams() throws ClassNotFoundException, SQLException, IOException {
        Collection<MetaFeature> ftrs = new ArrayList<>();
        Collection<MetaPerformance> pfms = new ArrayList<>();
        new SqlLoader(dbcs).load(ftrs, pfms, LocalDate.now(), TestData.ldtCreate.toLocalDate());
        assertEquals(2, ftrs.size());
        assertEquals(2, pfms.size());
    }
    
    @Test
    public void testLoadOnlyFeatures() throws IOException, SQLException {
        Collection<MetaFeature> ftrs = new ArrayList<>();
        new SqlLoader(dbcs).load(ftrs, null, TestData.ldtCreate.toLocalDate(), LocalDate.now());
        assertEquals(2, ftrs.size());
    }
}
