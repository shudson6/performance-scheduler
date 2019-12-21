package performancescheduler.data.storage.sql;

import java.util.Arrays;
import java.util.List;

import performancescheduler.data.storage.MetaPerformance;

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
                return Integer.toString(subject.getAuditorium().getNumber());
            case SQL.COL_FEATUREID:
                // TODO: How to get the uuid for the feature
                return "TODO";
            default:
                return super.colValue(col, subject);
        }
    }

    private final String[] COL_ORDER = new String[] {
            SQL.COL_UUID, SQL.COL_DATETIME, SQL.COL_AUDITORIUM, SQL.COL_FEATUREID,
            SQL.COL_CREATED, SQL.COL_CHANGED, SQL.COL_ACTIVE
    };
}
