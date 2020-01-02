package performancescheduler.data.storage.sql;

import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.MetaPerformance;

/**
 * Class used to create SQL INSERT commands for performance data.
 * @author Steven Hudson
 */
public class PsqlInsertPerformanceBuilder extends PsqlInsertBuilder<MetaPerformance> {
    
    /**
     * Create a new instance.
     * @param tableName name of the database table performance data will be written to
     */
    public PsqlInsertPerformanceBuilder(String tableName) {
        super(new PerformanceValueLister(), tableName);
    }
    
    @Override
    public boolean add(MetaPerformance toAdd) {
        if (toAdd != null && toAdd.getFeature() instanceof MetaFeature) {
            return super.add(toAdd);
        }
        return false;
    }
}
