package performancescheduler.data.storage.sql;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaWrapper;

class PsqlInsertFeatureBuilder extends SqlCommandBuilder<MetaFeature> {
    private FeatureValueLister values = new FeatureValueLister();
    
    public boolean add(MetaFeature ftr) {
        if (ftr != null && !ftr.getTitle().equals(MetaWrapper.NULLSTR)) {
            return super.add(ftr);
        }
        return false;
    }
    
    protected String buildCommand() {
        if (getData().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(SQL.TBL_FEATURE);
        sb.append(" VALUES ");
        getData().forEach(f -> sb.append(values.listValues(f) + ","));
        sb.replace(sb.lastIndexOf(","), sb.length(), " ");
        sb.append(conflictClause());
        sb.append(";");
        return sb.toString();
    }
    
    private String conflictClause() {
        StringBuilder sb = new StringBuilder();
        sb.append("ON CONFLICT (");
        sb.append(SQL.COL_UUID);
        sb.append(") DO UPDATE SET ");
        values.columnOrder().stream().filter(s -> !s.equals(SQL.COL_UUID))
                .forEach(s -> sb.append(s + "=EXCLUDED." + s + ","));
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}
