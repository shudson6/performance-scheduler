package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;

public class PerformanceManager extends ScheduleDataManager<Performance> {
    public final PerformanceFactory performanceFactory = PerformanceFactory.newFactory();
    
    public PerformanceManager() {
        super();
        // TODO build a more appropriate data structure for this
        data = new ArrayList<>();
    }

    @Override
    public Collection<Performance> getData() {
        // TODO change this once there is a better data structure in place for data
        return new ArrayList<>(data);
    }

    @Override
    public void setData(Collection<Performance> newData) {
        ArrayList<Performance> old = new ArrayList<>(data);
        data.clear();
        if (newData != null) {
            data.addAll(newData);
        }
        if (areEventsEnabled()) {
            fireEvent(eventFactory.newRemoveEvent(old));
            fireEvent(eventFactory.newAddEvent(data));
        }
    }
}
