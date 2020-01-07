package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;

public class FeatureDataModel extends ScheduleDataModel<Feature> {
    @Override
    public List<Feature> getData() {
        return new ArrayList<>(data);
    }
    
    @Override
    protected Collection<Feature> initData() {
        return new TreeSet<>();
    }
}
