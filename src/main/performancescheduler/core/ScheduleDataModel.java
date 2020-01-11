package performancescheduler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import performancescheduler.core.event.ScheduleEventFactory;
import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.data.Feature;

public abstract class ScheduleDataModel<T> {
    private List<ScheduleDataListener<T>> listenerList = new ArrayList<>();
    private boolean eventsEnabled = true;
    protected final ScheduleEventFactory eventFactory = ScheduleEventFactory.newFactory();
    
    protected Collection<T> data;
    
    protected ScheduleDataModel() {
        data = initData();
    }
    
    public boolean add(T toAdd) {
        boolean result = data.add(toAdd);
        if (result) {
            fireAddEvent(toAdd);
        }
        return result;
    }
    
    public boolean add(Collection<T> toAdd) {
    	Collection<T> added = new ArrayList(toAdd.size());
    	for (T t : toAdd) {
    		if (data.add(t)) {
    			added.add(t);
    		}
    	}
    	if (!added.isEmpty()) {
    		fireAddEvent(added);
    	}
    	return !added.isEmpty();
    }
    
    public void addScheduleDataListener(ScheduleDataListener<T> listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }
    
    public boolean areEventsEnabled() {
        return eventsEnabled;
    }
    
    public boolean contains(T element) {
        return data.contains(element);
    }
    
    public void fireAddEvent(T added) {
        fireAddEvent(Arrays.asList(added));
    }
    
    public void fireAddEvent(Collection<T> added) {
    	if (eventsEnabled) {
    		fireEvent(eventFactory.newAddEvent(added));
    	}
    }
    
    public void fireEvent(ScheduleEvent<T> event) {
        if (eventsEnabled) {
            listenerList.forEach(l -> l.scheduleDataChanged(event));
        }
    }
    
    public void fireRemoveEvent(T removed) {
        fireRemoveEvent(Arrays.asList(removed));
    }
    
    public void fireRemoveEvent(Collection<T> removed) {
    	if (eventsEnabled) {
    		fireEvent(eventFactory.newRemoveEvent(removed));
    	}
    }
    
    public void fireUpdateEvent(T before, T after) {
            fireUpdateEvent(Arrays.asList(before), Arrays.asList(after));
    }
    
    public void fireUpdateEvent(Collection<T> before, Collection<T> after) {
    	if (eventsEnabled) {
    		fireEvent(eventFactory.newUpdateEvent(before, after));
    	}
    }
    
    public abstract Collection<T> getData();
    
    protected abstract Collection<T> initData();
    
    public boolean remove(T toRm) {
        boolean result = data.remove(toRm);
        if (result) {
            fireRemoveEvent(toRm);
        }
        return result;
    }
    
    public boolean remove(Collection<T> toRm) {
    	Collection<T> removed = new ArrayList<>(toRm.size());
    	for (T t : toRm) {
    		if (data.remove(t)) {
    			removed.add(t);
    		}
    	}
    	if (!removed.isEmpty()) {
    		fireRemoveEvent(removed);
    	}
    	return !removed.isEmpty();
    }
    
    public void removeScheduleDataListener(ScheduleDataListener<T> listener) {
        listenerList.remove(listener);
    }
    
    public void setData(Collection<T> newData) {
        Collection<T> old = data;
        data = initData();
        if (areEventsEnabled() && !old.isEmpty()) {
            fireEvent(eventFactory.newRemoveEvent(old));
        }
        if (newData != null) {
            data.addAll(newData);
            if (areEventsEnabled() && !data.isEmpty()) {
                fireEvent(eventFactory.newAddEvent(data));
            }
        }
    }
    
    public void setEventsEnabled(boolean enable) {
        eventsEnabled = enable;
    }
    
    public int size() {
        return data.size();
    }
    
    /**
     * Updates an element by replacing it in the underlying structure. Like {@link #add()} and {@link #remove()},
     * the return value indicates whether the structure changed as a result of the operation. However, an update
     * involves removal and addition and it cannot be guaranteed that both operations succeed. By the default behavior
     * of this method, a return value of {@code true} indicates that the {@code before} element was removed, but says
     * nothing about the subsequent addition of the {@code after} element.
     * 
     * <p>The default behavior of this method is to first remove {@code before}. If that succeeds, then the attempt is
     * made to add {@code after}. If that succeeds, then an update event is fired--otherwise, a remove event is fired
     * instead. In either case, as long as the initial remove succeeded, the return value will be {@code true}.
     * 
     * @param before the element to be updated
     * @param after the new value of the element
     * @return {@code true} if the underlying data structure changed as a result of the operation
     */
    public boolean update(T before, T after) {
        if (data.remove(before)) {
            if (data.add(after)) {
                fireUpdateEvent(before, after);
            } else {
                fireRemoveEvent(before);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Updates the model by replacing the parameter's keys with their correspoding values. The input is assumed to be
     * a mapping of previous forms (keys) to their new forms (values). If any key does not exist in the model, the
     * value is not added (there is nothing to update). As with the other model-modifying methods, this method returns
     * {@code true} if the model is changed by the operation and notifies all listeners of any change.
     * 
     * @param map mapping of (oldForm, newForm) pairs
     * @return {@code true} if the model is changed by the operation
     */
    public boolean update(Map<T, T> map) {
    	Collection<T> before = new ArrayList<>(map.size());
    	Collection<T> after = new ArrayList<>(map.size());
    	map.forEach((b, a) -> {
    		if (data.remove(b)) {
    			before.add(b);
    			after.add(a);
    		}
    	});
    	// wait until the remove operations finish before adding anything, to avoid collisions where an added value
    	// could end up being removed
    	data.addAll(after);
    	if (before.size() > 0) {
    		fireUpdateEvent(before, after);
    		return true;
    	} else {
    		return false;
    	}
    }
}
