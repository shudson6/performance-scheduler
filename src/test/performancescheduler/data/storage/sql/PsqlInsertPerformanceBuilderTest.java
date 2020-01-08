package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.junit.Test;

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

public class PsqlInsertPerformanceBuilderTest {
    UUID uuid = new UUID(0xabcd987612341234L, 0x1234abcdef567890L);
    LocalDateTime ldt = LocalDateTime.of(2020, 3, 27, 19, 15);
    PsqlInsertBuilder<MetaPerformance> pInsert = new PsqlInsertPerformanceBuilder(TestData.TEST_TBL_PERFORMANCE);
    FeatureFactory ftrFac = FeatureFactory.newFactory();
    PerformanceFactory pfmFac = PerformanceFactory.newFactory();
    Auditorium aud = Auditorium.getInstance(1, null, false, 100);
    PerformanceValueLister pvl = new PerformanceValueLister();
    Feature ftr = ftrFac.createFeature("Foo", Rating.R, 90, false, false, false, false);
    
    @Test
    public void dontAcceptPerformanceWithNonMetaFeature() {
        Performance pfm = pfmFac.createPerformance(ftr, ldt, aud.getNumber(), 0, 0, 0);
        MetaPerformance mp = new TestMetaPerformance(pfm, uuid, ldt, ldt);
        pInsert.clear();
        assertFalse(pInsert.add(mp));
    }
    
    @Test
    public void dontAcceptNullPerformance() {
        assertFalse(pInsert.add(null));
    }
    
    @Test
    public void dontAcceptNullWrapped() {
        assertFalse(pInsert.add(new TestMetaPerformance(null, uuid, ldt, ldt)));
    }
    
    @Test
    public void insertSinglePerformance() {
        MetaFeature mf = new TestMetaFeature(ftr, uuid, ldt, null);
        Performance pfm = pfmFac.createPerformance(mf, ldt, aud.getNumber(), 0, 0, 0);
        MetaPerformance mp = new TestMetaPerformance(pfm, uuid, ldt, ldt);
        pInsert.clear();
        assertTrue(pInsert.add(mp));
        Collection<MetaPerformance> pfms = new ArrayList<>();
        pfms.add(mp);
        assertEquals(testString(pfms), pInsert.getCommand());
    }
    
    private String testString(Collection<MetaPerformance> mps) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + TestData.TEST_TBL_PERFORMANCE + " (");
        pvl.columnOrder().forEach(s -> sb.append(s + ","));
        sb.replace(sb.lastIndexOf(","), sb.length(), ") ");
        sb.append("VALUES ");
        for (MetaPerformance mp : mps) {
            if (mp != null && mp.getWrapped() != null) {
                sb.append("(");
                for (String s : pvl.columnOrder()) {
                    switch (s) {
                        case SQL.COL_DATETIME:
                            sb.append("'" + mp.getDateTime().format(SQL.DATETIME_FMT) + "'");
                            break;
                        case SQL.COL_AUDITORIUM:
                            sb.append(mp.getAuditorium());
                            break;
                        case SQL.COL_FEATUREID:
                            sb.append("'" + ((MetaFeature) mp.getFeature()).getUuid().toString() + "'");
                            break;
                        case SQL.COL_UUID:
                            sb.append("'" + mp.getUuid().toString() + "'");
                            break;
                        case SQL.COL_CHANGED:
                            sb.append("'" + mp.getChangedTimestamp().format(SQL.DATETIME_FMT) + "'");
                            break;
                        case SQL.COL_CREATED:
                            sb.append("'" + mp.getCreatedTimestamp().format(SQL.DATETIME_FMT) + "'");
                            break;
                        case SQL.COL_ACTIVE:
                            sb.append(true);
                            break;
                        case SQL.COL_SEATING:
                            sb.append(mp.getSeating());
                            break;
                        case SQL.COL_CLEANUP:
                            sb.append(mp.getCleanup());
                            break;
                        case SQL.COL_TRAILER:
                            sb.append(mp.getTrailers());
                    }
                    sb.append(",");
                }
                sb.replace(sb.lastIndexOf(","), sb.length(), ")");
            }
            sb.append(",");
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        // the conflict clause
        sb.append("ON CONFLICT (" + SQL.COL_UUID + ") DO UPDATE SET ");
        pvl.columnOrder().stream().filter(s -> !s.equals(SQL.COL_UUID) && !s.equals(SQL.COL_CREATED))
                .forEach(s -> sb.append(s + "=EXCLUDED." + s + ","));
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        sb.append("WHERE " + TestData.TEST_TBL_PERFORMANCE + "." + SQL.COL_UUID + "=EXCLUDED." + SQL.COL_UUID + " ");
        sb.append("AND " + TestData.TEST_TBL_PERFORMANCE + "." + SQL.COL_CHANGED + "<>EXCLUDED." + SQL.COL_CHANGED);
        sb.append(";");
        return sb.toString();
    }
}
