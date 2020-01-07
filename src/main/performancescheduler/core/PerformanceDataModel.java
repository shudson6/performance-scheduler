package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;

public class PerformanceDataModel extends ScheduleDataModel<Performance> {
    @Override
    public Collection<Performance> getData() {
        // TODO change this once there is a better data structure in place for data
        return new ArrayList<>(data);
    }
    
    @Override
    protected Collection<Performance> initData() {
        // TODO create a better data structure for this purpose
        return new ArrayList<>();
    }
}
