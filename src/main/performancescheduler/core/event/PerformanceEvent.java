package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Performance;

public class PerformanceEvent extends ScheduleEvent<Performance> {
	public static PerformanceEvent newAddEvent(Performance p) {
		Objects.requireNonNull(p, "PerformanceEvent.newAddEvent: nothing to add.");
		return new PerformanceEvent(wrapObject(p), null, ADD);
	}
	
	public static PerformanceEvent newAddEvent(Collection<Performance> p) {
		Objects.requireNonNull(p, "PerformanceEvent.newAddEvent: collection must not be null.");
		return new PerformanceEvent(p, null, ADD);
	}
	
	public static PerformanceEvent newRemoveEvent(Performance p) {
		Objects.requireNonNull(p, "PerformanceEvent.newRemoveEvent: nothing to remove.");
		return new PerformanceEvent(null, wrapObject(p), REMOVE);
	}
	
	public static PerformanceEvent newRemoveEvent(Collection<Performance> p) {
		Objects.requireNonNull(p, "PerformanceEvent.newRemoveEvent: collection must not be null.");
		return new PerformanceEvent(null, p, REMOVE);
	}
	
	public static PerformanceEvent newReplaceEvent(Performance add, Performance rem) {
		Objects.requireNonNull(add, "PerformanceEvent.newReplaceEvent: add must not be null.");
		Objects.requireNonNull(rem, "PerformanceEvent.newReplaceEvent: rem must not be null.");
		return new PerformanceEvent(wrapObject(add), wrapObject(rem), REPLACE);
	}
	
	public static PerformanceEvent newReplaceEvent(Collection<Performance> add, Collection<Performance> rem) {
		Objects.requireNonNull(add, "PerformanceEvent.newReplaceEvent: add must not be null.");
		Objects.requireNonNull(rem, "PerformanceEvent.newReplaceEvent: rem must not be null.");
		return new PerformanceEvent(add, rem, REPLACE);
	}
	
	private PerformanceEvent(Collection<Performance> add, Collection<Performance> rem, int action) {
		super(add, rem, action);
	}
}
