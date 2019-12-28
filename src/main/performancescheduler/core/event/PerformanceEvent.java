package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Performance;

public class PerformanceEvent extends ScheduleEvent<Performance> {	
	PerformanceEvent(Collection<Performance> add, Collection<Performance> rem, int action) {
		super(add, rem, action);
	}
}
