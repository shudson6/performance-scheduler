package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;

public class FeatureManager extends ScheduleDataManager<Feature> {
    private final FeatureFactory featureFactory = FeatureFactory.newFactory();
    
    @Override
    public List<Feature> getData() {
        return new ArrayList<>(data);
    }
    
    public FeatureFactory getFeatureFactory() {
        return featureFactory;
    }
    
    @Override
    protected Collection<Feature> initData() {
        return new TreeSet<>();
    }

    @Override
    public void setData(Collection<Feature> newData) {
        Collection<Feature> old = data;
        data = initData();
        if (areEventsEnabled() && !old.isEmpty()) {
            fireEvent(eventFactory.newRemoveEvent(old));
        }
        if (newData != null) {
            data.addAll(newData);
            if (areEventsEnabled() && !data.isEmpty()) {
                fireEvent(eventFactory.newAddEvent(data));
            }
        }
    }
}
