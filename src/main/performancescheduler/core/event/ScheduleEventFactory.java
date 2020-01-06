package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class ScheduleEventFactory {
    public static ScheduleEventFactory newFactory() {
        return new ScheduleEventFactory();
    }
	
	public <T> ScheduleEvent<T> newAddEvent(Collection<T> t) {
	    Objects.requireNonNull(t);
	    return new ScheduleEventImpl<T>(t, null, ScheduleEvent.ADD);
	}
	
	public <T> ScheduleEvent<T> newRemoveEvent(Collection<T> t) {
	    Objects.requireNonNull(t);
	    return new ScheduleEventImpl<T>(null, t, ScheduleEvent.REMOVE);
	}
	
	public <T> ScheduleEvent<T> newUpdateEvent(Collection<T> after, Collection<T> before) {
	    Objects.requireNonNull(after);
	    Objects.requireNonNull(before);
	    return new ScheduleEventImpl<T>(after, before, ScheduleEvent.UPDATE);
	}
	
	private ScheduleEventFactory() {}
}
