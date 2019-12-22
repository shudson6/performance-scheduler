package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.Test;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaWrapper;

public class PsqlInsertFeatureBuilderTest {
    FeatureFactory ftrFactory = FeatureFactory.newFactory();
    UUID uuid = new UUID(0xabcd987612341234L, 0x1234abcdef567890L);
    LocalDateTime ldt = LocalDateTime.of(2020, 3, 27, 19, 15);
    PsqlInsertBuilder<MetaFeature> pInsert = new PsqlInsertBuilder<>(new FeatureValueLister(), SQL.TBL_FEATURE);
    FeatureValueLister fvl = new FeatureValueLister();

    @Test
    public void insertSingleFeature() {
        Feature ftr = ftrFactory.createFeature("Foo", Rating.R, 90, false, true, true, true);
        pInsert.clear();
        pInsert.add(new TestMetaFeature(ftr, uuid, ldt, ldt));
        assertEquals("INSERT INTO featuredata VALUES ('abcd9876-1234-1234-1234-abcdef567890','Foo','R',90,"
                + "false,true,true,true,'2020-03-27 19:15:00','2020-03-27 19:15:00',true) ON CONFLICT (uuid) DO UPDATE "
                + "SET title=EXCLUDED.title,rating=EXCLUDED.rating,runtime=EXCLUDED.runtime,is3d=EXCLUDED.is3d,"
                + "cc=EXCLUDED.cc,oc=EXCLUDED.oc,da=EXCLUDED.da,changed=EXCLUDED.changed,active=EXCLUDED.active "
                + "WHERE featuredata.uuid=EXCLUDED.uuid AND featuredata.changed<>EXCLUDED.changed;",
                pInsert.getCommand());
    }
    
    @Test
    public void insertTwoFeatures() {
        Feature ftr = ftrFactory.createFeature("Foo", Rating.R, 90, false, true, true, true);
        pInsert.clear();
        TreeSet<MetaFeature> mfs = new TreeSet<>();
        mfs.add(new TestMetaFeature(ftr, uuid, ldt, null));
        ftr = ftrFactory.createFeature("Bar", Rating.PG13, 110, false, true, false, false);
        mfs.add(new TestMetaFeature(ftr, uuid, ldt, null));
        mfs.forEach(f -> pInsert.add(f));
        assertEquals(testString(mfs), pInsert.getCommand());
    }
    
    @Test
    public void emptyCommand() {
        pInsert.clear();
        pInsert.add(null);
        pInsert.add(new TestMetaFeature(null, uuid, ldt, null));
        assertTrue(pInsert.getCommand().isEmpty());
    }

    private String testString(Collection<MetaFeature> features) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + SQL.TBL_FEATURE + " VALUES ");
        for (MetaFeature mf : features) {
            if (mf != null && !mf.getTitle().equals(MetaWrapper.NULLSTR)) {
                sb.append("(");
                for (String s : fvl.columnOrder()) {
                    switch (s) {
                        case SQL.COL_TITLE:
                            sb.append("'" + mf.getTitle() + "'");
                            break;
                        case SQL.COL_RATING:
                            sb.append("'" + mf.getRating().toString() + "'");
                            break;
                        case SQL.COL_RUNTIME:
                            sb.append(mf.getRuntime());
                            break;
                        case SQL.COL_IS3D:
                            sb.append(mf.is3d());
                            break;
                        case SQL.COL_CC:
                            sb.append(mf.hasClosedCaptions());
                            break;
                        case SQL.COL_OC:
                            sb.append(mf.hasOpenCaptions());
                            break;
                        case SQL.COL_DA:
                            sb.append(mf.hasDescriptiveAudio());
                            break;
                        case SQL.COL_UUID:
                            sb.append("'" + mf.getUuid().toString() + "'");
                            break;
                        case SQL.COL_CHANGED:
                            sb.append("'" + mf.getChangedTimestamp().format(SQL.DATETIME_FMT) + "'");
                            break;
                        case SQL.COL_CREATED:
                            sb.append("'" + mf.getCreatedTimestamp().format(SQL.DATETIME_FMT) + "'");
                            break;
                        case SQL.COL_ACTIVE:
                            sb.append(true);
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
        fvl.columnOrder().stream().filter(s -> !s.equals(SQL.COL_UUID) && !s.equals(SQL.COL_CREATED))
                .forEach(s -> sb.append(s + "=EXCLUDED." + s + ","));
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        sb.append("WHERE " + SQL.TBL_FEATURE + "." + SQL.COL_UUID + "=EXCLUDED." + SQL.COL_UUID + " ");
        sb.append("AND " + SQL.TBL_FEATURE + "." + SQL.COL_CHANGED + "<>EXCLUDED." + SQL.COL_CHANGED);
        sb.append(";");
        return sb.toString();
    }
}
