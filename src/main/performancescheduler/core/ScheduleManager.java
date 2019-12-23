package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

abstract class ScheduleManager<T> {
	protected Collection<T> data = createDataStructure();
	private List<ScheduleListener> listenerList = new ArrayList<>();
	
	protected abstract Collection<T> createDataStructure();
	
	public boolean add(T item) {
		if (item != null) {
			data.add(item);
			// fire event
		}
		return false;
	}
	
	public boolean addAll(Collection<T> items) {
		if (items != null) {
			// add all items to data, keeping up with which ones are added,
			// and fire event about it
		}
		return false;
	}
	
	public void addScheduleListener(ScheduleListener listener) {
		if (!listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}
	
	public void removeScheduleListener(ScheduleListener listener) {
		listenerList.remove(listener);
	}
}
