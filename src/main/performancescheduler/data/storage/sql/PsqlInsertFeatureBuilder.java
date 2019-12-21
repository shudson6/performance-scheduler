package performancescheduler.data.storage.sql;

import java.util.Collection;
import java.util.TreeSet;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaWrapper;

class PsqlInsertFeatureBuilder {
    private Collection<MetaFeature> features;
    private FeatureValueLister values;
    
    public PsqlInsertFeatureBuilder() {
        features = new TreeSet<>();
        values = new FeatureValueLister();
    }
    
    public void clear() {
        features.clear();
    }
    
    public boolean add(MetaFeature ftr) {
        if (ftr != null && !ftr.getTitle().equals(MetaWrapper.NULLSTR)) {
            return features.add(ftr);
        }
        return false;
    }
    
    public String getCommand() {
        return buildCommand();
    }
    
    protected String buildCommand() {
        if (features.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO FEATUREDATA VALUES ");
        features.forEach(f -> sb.append(values.listValues(f) + ","));
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
