package performancescheduler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import performancescheduler.core.event.EventFactory;
import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;

public abstract class ScheduleDataManager<T> {
    private List<ScheduleDataListener<T>> listenerList = new ArrayList<>();
    private boolean eventsEnabled = true;
    protected final EventFactory eventFactory = EventFactory.newFactory();
    
    protected Collection<T> data;
    
    protected ScheduleDataManager() {
        data = initData();
    }
    
    public boolean add(T toAdd) {
        boolean result = data.add(toAdd);
        if (result) {
            fireAddEvent(toAdd);
        }
        return result;
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
        if (eventsEnabled) {
            fireEvent(eventFactory.newAddEvent(Arrays.asList(added)));
        }
    }
    
    public void fireEvent(ScheduleEvent<T> event) {
        if (eventsEnabled) {
            listenerList.forEach(l -> l.scheduleDataChanged(event));
        }
    }
    
    public void fireRemoveEvent(T removed) {
        if (eventsEnabled) {
            fireEvent(eventFactory.newRemoveEvent(Arrays.asList(removed)));
        }
    }
    
    public void fireUpdateEvent(T after, T before) {
        if (eventsEnabled) {
            fireEvent(eventFactory.newUpdateEvent(Arrays.asList(after), Arrays.asList(before)));
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
    
    public void removeScheduleDataListener(ScheduleDataListener<T> listener) {
        listenerList.remove(listener);
    }
    
    public abstract void setData(Collection<T> newData);
    
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
                fireUpdateEvent(after, before);
            } else {
                fireRemoveEvent(before);
            }
            return true;
        }
        return false;
    }
}
