package performancescheduler.data.storage.sql;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaWrapper;

public class PsqlDeactivateFeatureBuilder extends SqlCommandBuilder<MetaFeature> {

    @Override
    public boolean add(MetaFeature toAdd) {
        if (toAdd != null && toAdd.getTitle().equals(MetaWrapper.NULLSTR)) {
            return super.add(toAdd);
        }
        return false;
    }
    
    @Override
    protected String buildCommand() {
        StringBuilder sb = new StringBuilder();
        getData().stream().filter(mf -> (mf != null) && mf.getTitle().equals(MetaWrapper.NULLSTR))
                .forEach(mf -> sb.append(delStr(mf)));
        return sb.toString();
    }
    
    private String delStr(MetaFeature mf) {
        return String.format("UPDATE %s SET %s=false,%s='%s' WHERE %s='%s';", SQL.TBL_FEATURE, SQL.COL_ACTIVE,
                SQL.COL_CHANGED, mf.getChangedTimestamp().format(SQL.DATETIME_FMT), SQL.COL_UUID, mf.getUuid());
    }
}
