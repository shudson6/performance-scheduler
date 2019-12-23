package performancescheduler.core;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class ScheduleEvent<T> {
	public static final int ADD = 0x0001;
	public static final int REMOVE = 0x0002;
	public static final int REPLACE = 0x0003;	// add && remove
	public static final int SINGLE = 0x0004;
	public static final int MULTIPLE = 0x0008;
	public static final int FEATURE = 0x0010;
	public static final int PERFORMANCE = 0x0020;
	
	public static <E> ScheduleEvent<E> addSingle(E added) {
		Objects.requireNonNull(added);
		return new ScheduleEvent<E>(added, null, null, null, ADD | SINGLE | typeMask(added));
	}
	
	public static <E> ScheduleEvent<E> addMultiple(Collection<E> added) {
		Objects.requireNonNull(added);
		return new ScheduleEvent<E>(null, added, null, null, ADD | MULTIPLE);
	}
	
	private static int typeMask(Object t) {
		if (t instanceof Feature) {
			return FEATURE;
		} else if (t instanceof Performance) {
			return PERFORMANCE;
		}
		return 0;
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
