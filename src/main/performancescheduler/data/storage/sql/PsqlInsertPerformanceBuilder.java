package performancescheduler.data.storage.sql;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

public class PsqlInsertPerformanceBuilder extends PsqlInsertBuilder<MetaPerformance> {
    public PsqlInsertPerformanceBuilder() {
        super(new PerformanceValueLister(), SQL.TBL_PERFORMANCE);
    }
    
    @Override
    public boolean add(MetaPerformance toAdd) {
        if (toAdd != null && toAdd.getFeature() instanceof MetaFeature) {
            return super.add(toAdd);
        }
        return false;
    }
}
