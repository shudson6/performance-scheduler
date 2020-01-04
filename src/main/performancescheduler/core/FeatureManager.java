package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;

public class FeatureManager extends ScheduleDataManager<Feature> {
    public final FeatureFactory featureFactory = FeatureFactory.newFactory();
    
    public FeatureManager() {
        super();
        data = new TreeSet<>();
    }
    
    @Override
    public List<Feature> getData() {
        return new ArrayList<>(data);
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
            fireEvent(eventFactory.newAddEvent(data));
        }
    }
}
