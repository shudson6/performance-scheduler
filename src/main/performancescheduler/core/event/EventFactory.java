package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class EventFactory {
    public static EventFactory newFactory() {
        return new EventFactory();
    }

    public FeatureEvent newAddFeatureEvent(Collection<Feature> f) {
        Objects.requireNonNull(f);
        return new FeatureEvent(f, null, ScheduleEvent.ADD);
    }
    
	public PerformanceEvent newAddPerformanceEvent(Collection<Performance> p) {
		Objects.requireNonNull(p);
		return new PerformanceEvent(p, null, ScheduleEvent.ADD);
	}
	
	public FeatureEvent newRemoveFeatureEvent(Collection<Feature> f) {
		Objects.requireNonNull(f, "FeatureEvent.newRemoveEvent: nothing to remove.");
		return new FeatureEvent(null, f, ScheduleEvent.REMOVE);
	}
	
	public PerformanceEvent newRemovePerformanceEvent(Collection<Performance> p) {
		Objects.requireNonNull(p);
		return new PerformanceEvent(null, p, ScheduleEvent.REMOVE);
	}
	
	public FeatureEvent newUpdateFeatureEvent(Collection<Feature> after, Collection<Feature> before) {
	    Objects.requireNonNull(before);
	    Objects.requireNonNull(after);
	    return new FeatureEvent(after, before, ScheduleEvent.UPDATE);
	}
	
	public PerformanceEvent newUpdatePerformanceEvent(Collection<Performance> after, Collection<Performance> before) {
	    Objects.requireNonNull(before);
	    Objects.requireNonNull(after);
	    return new PerformanceEvent(after, before, ScheduleEvent.UPDATE);
	}
}
