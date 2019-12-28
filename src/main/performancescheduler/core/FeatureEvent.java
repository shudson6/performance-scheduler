package performancescheduler.core;

import java.util.Collection;

import performancescheduler.data.Feature;

public class FeatureEvent extends ScheduleEvent<Feature> {
	public FeatureEvent(Feature add, Feature rem) {
		super(add, rem, FEATURE);
	}
	
	public FeatureEvent(Collection<Feature> add, Collection<Feature> rem) {
		super(add, rem, FEATURE);
	}
}
