package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;

public class FeatureEvent extends ScheduleEvent<Feature> {	
	FeatureEvent(Collection<Feature> add, Collection<Feature> rem, int action) {
		super(add, rem, action);
	}
}
