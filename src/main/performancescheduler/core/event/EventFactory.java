package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class EventFactory {
    public static EventFactory newFactory() {
        return new EventFactory();
    }
	
	public <T> ScheduleEvent<T> newAddEvent(Collection<T> t) {
	    Objects.requireNonNull(t);
	    return new ScheduleEvent<T>(t, null, ScheduleEvent.ADD);
	}
	
	public <T> ScheduleEvent<T> newRemoveEvent(Collection<T> t) {
	    Objects.requireNonNull(t);
	    return new ScheduleEvent<T>(null, t, ScheduleEvent.REMOVE);
	}
	
	public <T> ScheduleEvent<T> newUpdateEvent(Collection<T> after, Collection<T> before) {
	    Objects.requireNonNull(after);
	    Objects.requireNonNull(before);
	    return new ScheduleEvent<T>(after, before, ScheduleEvent.UPDATE);
	}
	
	private EventFactory() {}
}
