package performancescheduler.gui;

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
    private Collection<Performance> data;
    private boolean eventsEnabled = true;
    
    public boolean add(Performance p) {
        boolean result = data.add(p);
        if (result) {
            fireAddEvent(p);
        }
        return result;
    }
    
    public boolean areEventsEnabled() {
        return eventsEnabled;
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
    
    public void fireReplaceEvent(Performance old, Performance add) {
        if (eventsEnabled) {
            fireEvent(GraphDataEvent.newReplaceEvent(old, add));
        }
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

    @Override
    public void scheduleDataChanged(ScheduleEvent<Performance> event) {
        switch (event.getAction()) {
            case GraphDataEvent.ADD:
                scheduleDataAdded(event.getAdded());
                break;
            case GraphDataEvent.REMOVE:
                scheduleDataRemoved(event.getRemoved());
                break;
            case GraphDataEvent.REPLACE:
                scheduleDataReplaced(event.getRemoved(), event.getAdded());
                break;
            default:
                // this should really never happen, but just silently do nothing
        }
    }
    
    public void setEventsEnabled(boolean enabled) {
        eventsEnabled = enabled;
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
        } else if (crem.isEmpty() && !cadd.isEmpty()) {
            fireEvent(GraphDataEvent.newAddEvent(cadd));
        }
        // if both are empty, there's nothing to fire
    }
}
