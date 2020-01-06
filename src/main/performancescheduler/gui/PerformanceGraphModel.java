package performancescheduler.gui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.data.Performance;
import performancescheduler.gui.event.GraphDataEvent;
import performancescheduler.gui.event.GraphDataListener;

public class PerformanceGraphModel implements Iterable<Performance>, ScheduleDataListener<Performance> {
    private List<GraphDataListener> listenerList = new ArrayList<>();
    private boolean eventsEnabled = true;
    private Collection<Performance> data;
    
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    
    public PerformanceGraphModel(LocalDateTime start, LocalDateTime end) {
        rangeStart = (start != null) ? start : LocalDateTime.MIN;
        rangeEnd = (end != null) ? end : LocalDateTime.MAX;
        if (Duration.between(rangeStart, rangeEnd).compareTo(Duration.ofHours(24)) < 0) {
            throw new IllegalStateException("Date range must span at least 24 hours.");
        }
        data = initData();
    }
    
    public boolean accept(Performance p) {
        return rangeStart.compareTo(p.getDateTime()) <= 0 && rangeEnd.compareTo(p.getDateTime()) > 0;
    }
    
    public boolean add(Performance p) {
        boolean result = accept(p) && data.add(p);
        if (result) {
            fireAddEvent(p);
        }
        return result;
    }
    
    public void addEventListener(GraphDataListener listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }
    
    public boolean areEventsEnabled() {
        return eventsEnabled;
    }
    
    public boolean contains(Performance p) {
        return data.contains(p);
    }
    
    public void fireAddEvent(Performance p) {
        if (eventsEnabled) {
            fireEvent(GraphDataEvent.newAddEvent(p));
        }
    }
    
    public void fireEvent(GraphDataEvent event) {
        if (eventsEnabled) {
            listenerList.forEach(l -> l.graphDataChanged(event));
        }
    }
    
    public void fireRemoveEvent(Performance p) {
        if (eventsEnabled) {
            fireEvent(GraphDataEvent.newRemoveEvent(p));
        }
    }
    
    public LocalDateTime getRangeEnd() {
        return rangeEnd;
    }
    
    public LocalDateTime getRangeStart() {
        return rangeStart;
    }
    
    protected Collection<Performance> initData() {
        return new ArrayList<>();
    }

    @Override
    public Iterator<Performance> iterator() {
        return data.iterator();
    }
    
    public boolean remove(Performance p) {
        boolean result = data.remove(p);
        if (result) {
            fireRemoveEvent(p);
        }
        return result;
    }
    
    public void removeEventListener(GraphDataListener listener) {
        listenerList.remove(listener);
    }

    @Override
    public void scheduleDataChanged(ScheduleEvent<Performance> event) {
        switch (event.getAction()) {
            case ScheduleEvent.ADD:
                scheduleDataAdded(event.getAdded());
                break;
            case ScheduleEvent.REMOVE:
                scheduleDataRemoved(event.getRemoved());
                break;
            case ScheduleEvent.UPDATE:
                scheduleDataReplaced(event.getRemoved(), event.getAdded());
                break;
            default:
                // this should really never happen, but just silently do nothing
        }
    }
    
    public void setEventsEnabled(boolean enabled) {
        eventsEnabled = enabled;
    }
    
    public int size() {
        return data.size();
    }

    private void scheduleDataAdded(Collection<Performance> add) {
        setEventsEnabled(false);
        Collection<Performance> c = new ArrayList<>();
        for (Performance p : add) {
            if (add(p)) {
                c.add(p);
            }
        }
        setEventsEnabled(true);
        if (!c.isEmpty()) {
            fireEvent(GraphDataEvent.newAddEvent(c));
        }
    }
    
    private void scheduleDataRemoved(Collection<Performance> rem) {
        setEventsEnabled(false);
        Collection<Performance> c = new ArrayList<>();
        for (Performance p : rem) {
            if (remove(p)) {
                c.add(p);
            }
        }
        setEventsEnabled(true);
        if (!c.isEmpty()) {
            fireEvent(GraphDataEvent.newRemoveEvent(c));
        }
    }
    
    private void scheduleDataReplaced(Collection<Performance> rem, Collection<Performance> add) {
        setEventsEnabled(false);
        Collection<Performance> crem = new ArrayList<>();
        Collection<Performance> cadd = new ArrayList<>();
        for (Performance p : rem) {
            if (remove(p)) {
                crem.add(p);
            }
        }
        for (Performance p : add) {
            if (add(p)) {
                cadd.add(p);
            }
        }
        setEventsEnabled(true);
        if (!crem.isEmpty() && !cadd.isEmpty()) {
            fireEvent(GraphDataEvent.newReplaceEvent(crem, cadd));
        } else if (!crem.isEmpty()) {
            fireEvent(GraphDataEvent.newRemoveEvent(crem));
        } else if (!cadd.isEmpty()) {
            fireEvent(GraphDataEvent.newAddEvent(cadd));
        }
        // if both are empty, there's nothing to fire
    }
}
