package performancescheduler.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScheduleEvent<T> {
	public static final int ADD = 0x0001;
	public static final int REMOVE = 0x0002;
	public static final int UPDATE = 0x0003;	// add && remove
	
	private final int action;
	private final List<T> added = new ArrayList<>();
	private final List<T> removed = new ArrayList<>();
	
	public int getAction() {
		return action;
	}
	
	public List<T> getAdded() {
		return new ArrayList<>(added);
	}
	
	public List<T> getRemoved() {
		return new ArrayList<>(removed);
	}
	
	ScheduleEvent(Collection<T> add, Collection<T> rem, int act) {
		action = validateAction(act);
		if (add != null) {
			added.addAll(add);
		}
		if (rem != null) {
			removed.addAll(rem);
		}
	}
	
	private static int validateAction(int action) {
		switch (action) {
			case ADD:
			case REMOVE:
			case UPDATE:
				return action;
			default:
				throw new IllegalArgumentException("Action must be one of ADD, REMOVE, UPDATE.");
		}
	}
}
