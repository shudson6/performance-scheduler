package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;

import performancescheduler.data.Performance;

public class PerformanceManager extends ScheduleDataManager<Performance> {

    @Override
    public Collection<Performance> getData() {
        // TODO change this once there is a better data structure in place for data
        return new ArrayList<>(data);
    }

    @Override
    public void setData(Collection<Performance> newData) {
        
    }
}
