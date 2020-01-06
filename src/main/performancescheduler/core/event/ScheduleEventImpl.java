package performancescheduler.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ScheduleEventImpl<T> implements ScheduleEvent<T> {
	private final int action;
	private final List<T> added = new ArrayList<>();
	private final List<T> removed = new ArrayList<>();
	
	@Override
    public int getAction() {
		return action;
	}
	
	@Override
    public List<T> getAdded() {
		return new ArrayList<>(added);
	}
	
	@Override
    public List<T> getRemoved() {
		return new ArrayList<>(removed);
	}
	
	ScheduleEventImpl(Collection<T> add, Collection<T> rem, int act) {
		action = validateAction(act);
		// if action is UPDATE, the collections must have the same number of items, so a 1-to-1 relationship
		// can be assumed
		if (action == UPDATE) {
			validateUpdate(add, rem);
		}
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
	
	private static void validateUpdate(Collection<?> add, Collection<?> rem) {
		Objects.requireNonNull(add);
		Objects.requireNonNull(rem);
		if (add.size() != rem.size()) {
			throw new IllegalArgumentException("Update before and after must be same size.");
		}
	}
}
