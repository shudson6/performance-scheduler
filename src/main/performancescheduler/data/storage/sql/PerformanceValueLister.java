package performancescheduler.data.storage.sql;

import java.util.Arrays;
import java.util.List;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

/**
 * Class used to enumerate the values that describe performance objects.
 * @author Steven Hudson
 */
public class PerformanceValueLister extends ValueLister<MetaPerformance> {

    @Override
    public List<String> columnOrder() {
        return Arrays.asList(COL_ORDER);
    }

    @Override
    String colValue(String col, MetaPerformance subject) {
        switch (col) {
            case SQL.COL_DATETIME:
                return quotes(subject.getDateTime().format(SQL.DATETIME_FMT));
            case SQL.COL_AUDITORIUM:
                return Integer.toString(subject.getAuditorium());
            case SQL.COL_FEATUREID:
                return featureId(subject);
            default:
                return super.colValue(col, subject);
        }
    }
    
    private String featureId(MetaPerformance mp) {
        if (mp.getFeature() instanceof MetaFeature) {
            return quotes(((MetaFeature) mp.getFeature()).getUuid().toString());
        } else {
            throw new IllegalArgumentException("MetaPerformance is required to have an instance of MetaFeature");
        }
    }

    private final String[] COL_ORDER = new String[] {
            SQL.COL_UUID, SQL.COL_DATETIME, SQL.COL_AUDITORIUM, SQL.COL_FEATUREID,
            SQL.COL_CREATED, SQL.COL_CHANGED, SQL.COL_ACTIVE
    };
}
