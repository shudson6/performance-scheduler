package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;

public class FeatureManager extends ScheduleDataManager<Feature> {
    public final FeatureFactory featureFactory = FeatureFactory.newFactory();
    
    @Override
    public List<Feature> getData() {
        return new ArrayList<>(data);
    }
    
    @Override
    protected Collection<Feature> initData() {
        return new TreeSet<>();
    }

    @Override
    public void setData(Collection<Feature> newData) {
        TreeSet<Feature> old = new TreeSet<>(data);
        data.clear();
        if (newData != null) {
            data.addAll(newData);
        }
        if (areEventsEnabled()) {
            fireEvent(eventFactory.newRemoveEvent(old));
            if (!data.isEmpty());
            fireEvent(eventFactory.newAddEvent(data));
        }
    }
}
