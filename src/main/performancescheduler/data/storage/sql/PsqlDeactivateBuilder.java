package performancescheduler.data.storage.sql;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;
import performancescheduler.data.storage.MetaWrapper;

public class PsqlDeactivateBuilder extends SqlCommandBuilder<MetaWrapper<?>> {

    @Override
    public boolean add(MetaWrapper<?> toAdd) {
        if (toAdd != null && toAdd.getWrapped() == null) {
            return super.add(toAdd);
        }
        return false;
    }
    
    @Override
    protected String buildCommand() {
        StringBuilder sb = new StringBuilder();
        getData().forEach(mw -> sb.append(delStr(mw)));
        return sb.toString();
    }
    
    private String delStr(MetaWrapper<?> mw) {
        String tbl;
        if (mw instanceof MetaFeature) {
            tbl = SQL.TBL_FEATURE;
        } else if (mw instanceof MetaPerformance) {
            tbl = SQL.TBL_PERFORMANCE;
        } else {
            return "";
        }
        return String.format("UPDATE %s SET %s=false,%s='%s' WHERE %s='%s';", tbl, SQL.COL_ACTIVE,
                SQL.COL_CHANGED, mw.getChangedTimestamp().format(SQL.DATETIME_FMT), SQL.COL_UUID, mw.getUuid());
    }
}
