package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import performancescheduler.TestData;

class SqlTestUtil {
    static void insertTestData() throws ClassNotFoundException, IOException, SQLException {
        DbConnectionService dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
        new SqlSaver(dbcs).save(Arrays.asList(TestData.mfBar, TestData.mfFoo),
                Arrays.asList(TestData.mpFoo1, TestData.mpBar2));
        dbcs.close();
    }

    static void removeTestData() throws SQLException, IOException, ClassNotFoundException {
        DbConnectionService dbcs = new DbConnectionService(TestData.PROPERTIES());
        dbcs.start();
        dbcs.getStatement().execute(String.format("DELETE FROM %s WHERE CAST(%s AS VARCHAR) LIKE '%%dead-beef%%';", 
                TestData.TEST_TBL_PERFORMANCE, SQL.COL_UUID));
        dbcs.getStatement().execute(String.format("DELETE FROM %s WHERE CAST(%s AS VARCHAR) LIKE '%%dead-beef%%';", 
                TestData.TEST_TBL_FEATURE, SQL.COL_UUID));
        dbcs.close();
    }
    
}
