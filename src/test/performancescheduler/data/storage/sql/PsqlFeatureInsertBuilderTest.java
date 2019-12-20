package performancescheduler.data.storage.sql;

import static org.junit.Assert.*;
import static performancescheduler.data.storage.sql.PsqlFeatureInsertBuilder.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.util.UUIDGenerator;

public class PsqlFeatureInsertBuilderTest {
    static FeatureFactory factory = FeatureFactory.newFactory();
    static UUIDGenerator idGen = new UUIDGenerator();
    
    @Test
    public void generateSingleUpsert() {
        MetaFeature mf = new TestMetaFeature(factory.createFeature("foo", Rating.NR, 90, false, false, false, false),
                idGen.generateUUID(), LocalDateTime.now(), null);
        Collection<MetaFeature> mfs = new ArrayList<>();
        mfs.add(mf);
        assertEquals(testString(mfs), new PsqlFeatureInsertBuilder().generateSQL(mfs));
    }
    
    private String testString(Collection<MetaFeature> mfs) {
        StringBuilder sb = new StringBuilder(ENTRY_SIZE * mfs.size() + SQL_SIZE);
        sb.append("INSERT INTO FEATUREDATA VALUES ");
        for (MetaFeature mf : mfs) {
            if (mf.getTitle() != null) {
                sb.append("('" + mf.getUuid().toString() + "',");
                sb.append("'" + mf.getTitle() + "','" + mf.getRating().toString() + "',");
                sb.append(mf.getRuntime() + "," + Boolean.toString(mf.is3d()) + ",");
                sb.append(Boolean.toString(mf.hasClosedCaptions()) + ",");
                sb.append(Boolean.toString(mf.hasOpenCaptions()) + ",");
                sb.append(Boolean.toString(mf.hasDescriptiveAudio()) + ",");
                sb.append("'" + mf.getCreatedTimestamp().toString() + "',");
                sb.append("'" + mf.getChangedTimestamp().toString() + "',");
                sb.append("true),");
            }
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        sb.append(ON_CONFLICT);
        for (MetaFeature mf : mfs) {
            if (mf.getTitle() == null) {
                sb.append("UPDATE FEATUREDATA SET active=false WHERE uuid='" + mf.getUuid().toString() + "';");
            }
        }
        return sb.toString();
    }
}
