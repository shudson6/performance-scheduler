package performancescheduler.core;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class ScheduleEvent<T> {
	public static final int NONE = 0;
	public static final int ADD = 0x0001;
	public static final int REMOVE = 0x0002;
	public static final int REPLACE = 0x0003;	// add && remove
	public static final int SINGLE = 0x0004;
	public static final int MULTIPLE = 0x0008;
	public static final int FEATURE = 0x0010;
	public static final int PERFORMANCE = 0x0020;
	
	public static <E> ScheduleEvent<E> emptyEvent() {
		return new ScheduleEvent<>(null, null, null, null, NONE);
	}
	
	public static <E> ScheduleEvent<E> newEvent(E entry, int action) {
		Objects.requireNonNull(entry);
		if (action == ADD) {
			return new ScheduleEvent<>(entry, null, null, null, ADD | SINGLE | typeMask(entry.getClass()));
		} else if (action == REMOVE) {
			return new ScheduleEvent<>(null, null, entry, null, REMOVE | SINGLE | typeMask(entry.getClass()));
		} else {
			throw new IllegalArgumentException("Action must be ADD or REMOVE.");
		}
	}
	
	public static <E> ScheduleEvent<E> newEvent(Collection<E> entries, int action) {
		Objects.requireNonNull(entries);
		if (!entries.isEmpty()) {
			if (action == ADD) {
				return new ScheduleEvent<>(null, entries, null, null, 
						ADD | MULTIPLE | typeMask(entries.iterator().next().getClass()));
			} else if (action == REMOVE) {
				return new ScheduleEvent<>(null, null, null, entries,
						REMOVE | MULTIPLE | typeMask(entries.iterator().next().getClass()));
			} else {
				throw new IllegalArgumentException("Action must be ADD or REMOVE.");
			}
		} else {
			throw new IllegalArgumentException("Event may not be empty.");
		}
	}
	
	private static <E> int typeMask(Class<E> clazz) {
		if (Feature.class.isAssignableFrom(clazz)) {
			return FEATURE;
		} else if (Performance.class.isAssignableFrom(clazz)) {
			return PERFORMANCE;
		}
		return NONE;
	}
	
	private final int eventType;
	private final T added;
	private final Collection<T> addedColl;
	private final T removed;
	private final Collection<T> removedColl;
	
	private ScheduleEvent(T a, Collection<T> ac, T r, Collection<T> rc, int i) {
		eventType = i;
		added = a;
		addedColl = ac;
		removed = r;
		removedColl = rc;
	}
}
