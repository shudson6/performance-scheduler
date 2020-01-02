package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class SqlLoaderTest {
    static DbConnectionService dbcs;
    
    @Test
    public void test() throws ClassNotFoundException, SQLException, IOException {
        Collection<MetaFeature> ftrs = new ArrayList<>();
        Collection<MetaPerformance> pfms = new ArrayList<>();
        dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
        new SqlLoader(dbcs).load(ftrs, pfms, TestData.ldtCreate.toLocalDate(), LocalDate.now());
        assertEquals(2, ftrs.size());
        assertEquals(2, pfms.size());
    }
}
