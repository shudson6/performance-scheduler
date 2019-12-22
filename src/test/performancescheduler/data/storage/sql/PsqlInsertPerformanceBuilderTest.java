package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Test;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaPerformance;

public class PsqlInsertPerformanceBuilderTest {
    UUID uuid = new UUID(0xabcd987612341234L, 0x1234abcdef567890L);
    LocalDateTime ldt = LocalDateTime.of(2020, 3, 27, 19, 15);
    PsqlInsertBuilder<MetaPerformance> pInsert = 
            new PsqlInsertBuilder<>(new PerformanceValueLister(), SQL.TBL_PERFORMANCE);
    FeatureFactory ftrFac = FeatureFactory.newFactory();
    PerformanceFactory pfmFac = PerformanceFactory.newFactory();
    Auditorium aud = Auditorium.getInstance(1, null, false, 100);
    
    @Test
    public void insertSinglePerformance() {
        Feature ftr = ftrFac.createFeature("Foo", Rating.R, 90, false, false, false, false);
        Performance pfm = pfmFac.createPerformance(ftr, ldt, aud);
        MetaPerformance mp = new TestMetaPerformance(pfm, uuid, ldt, ldt);
        pInsert.clear();
        pInsert.add(mp);
        String str = String.format("INSERT INTO performancedata VALUES ('%s','%s',%d,'%s','%s','%s',true) "
                + "ON CONFLICT (uuid) DO UPDATE SET datetime=EXCLUDED.datetime,auditorium=EXCLUDED.auditorium,"
                + "feature=EXCLUDED.feature,created=EXCLUDED.created,changed=EXCLUDED.changed,"
                + "active=true", mp.getUuid().toString(), mp.getDateTime().format(SQL.DATETIME_FMT),
                mp.getAuditorium().getNumber(), uuid.toString(), mp.getCreatedTimestamp().format(SQL.DATETIME_FMT),
                mp.getChangedTimestamp().format(SQL.DATETIME_FMT));
        assertEquals(str, pInsert.getCommand());
    }
}
