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
    
    public boolean update(T before, T after) {
        if (data.remove(before)) {
            data.add(after);
            fireUpdateEvent(after, before);
            return true;
        }
        return false;
    }
}
