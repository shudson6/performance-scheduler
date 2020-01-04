package performancescheduler.core;

import java.util.Arrays;
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
        return Arrays.asList(data.toArray(new Feature[data.size()]));
    }

    @Override
    public void setData(Collection<Feature> newData) {
        data.clear();
        if (newData != null) {
            data.addAll(newData);
        }
    }
}
