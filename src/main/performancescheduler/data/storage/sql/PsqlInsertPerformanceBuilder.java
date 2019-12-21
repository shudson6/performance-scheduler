package performancescheduler.data.storage.sql;

import performancescheduler.data.storage.MetaPerformance;

public class PsqlInsertPerformanceBuilder extends SqlCommandBuilder<MetaPerformance> {

    @Override
    public boolean add(MetaPerformance toAdd) {
        if (toAdd != null && toAdd.getWrapped() != null) {
            return super.add(toAdd);
        }
        return false;
    }
    
    @Override
    protected String buildCommand() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
