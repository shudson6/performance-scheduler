package performancescheduler.gui;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.list.TreeList;

import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.data.Performance;
import performancescheduler.gui.event.GraphDataEvent;
import performancescheduler.gui.event.GraphDataListener;

public class PerformanceGraphModel implements Iterable<Performance>, ScheduleDataListener<Performance> {
    private List<GraphDataListener> listenerList = new ArrayList<>();
    private boolean eventsEnabled = true;
    private List<Performance> data;
    
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    
    public PerformanceGraphModel(LocalDateTime start, LocalDateTime end) {
        rangeStart = (start != null) ? start : LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 0));
        rangeEnd = (end != null) ? end
                : LocalDateTime.of(rangeStart.toLocalDate().plusDays(1), rangeStart.toLocalTime());
        if (Duration.between(rangeStart, rangeEnd).compareTo(Duration.ofHours(24)) < 0) {
            throw new IllegalStateException("Date range must span at least 24 hours.");
        }
        data = initData();
    }
    
    public boolean accept(Performance p) {
        return rangeStart.compareTo(p.getDateTime()) <= 0 && rangeEnd.compareTo(p.getDateTime()) > 0;
    }
    
    public boolean add(Performance p) {
        boolean result = accept(p);
        if (result) {
            data.add(insertionIndex(p), p);
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
        if (eventsEnabled && event != null) {
            listenerList.forEach(l -> l.graphDataChanged(event));
        }
    }
    
    public void fireRemoveEvent(Performance p) {
        if (eventsEnabled) {
            fireEvent(GraphDataEvent.newRemoveEvent(p));
        }
    }
    
    public Performance getElementAt(int index) {
    	return data.get(index);
    }
    
    public LocalDateTime getRangeEnd() {
        return rangeEnd;
    }
    
    public LocalDateTime getRangeStart() {
        return rangeStart;
    }
    
    public int indexOf(Performance p) {
    	return data.indexOf(p);
    }
    
    protected List<Performance> initData() {
        return new TreeList<>();
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
        GraphDataEvent gde = null;
        boolean before = eventsEnabled;
        setEventsEnabled(false);
        switch (event.getAction()) {
            case ScheduleEvent.ADD:
                gde = scheduleDataAdded(event.getAdded());
                break;
            case ScheduleEvent.REMOVE:
                gde = scheduleDataRemoved(event.getRemoved());
                break;
            case ScheduleEvent.UPDATE:
                gde = scheduleDataReplaced(event.getRemoved(), event.getAdded());
                break;
            default:
                // this should really never happen, but just silently do nothing
        }
        setEventsEnabled(before);
        fireEvent(gde);
    }
    
    public void setEventsEnabled(boolean enabled) {
        eventsEnabled = enabled;
    }
    
    public int size() {
        return data.size();
    }
    
    private int insertionIndex(Performance p) {
        return insertionIndex(p, 0, data.size());
    }
    
    private int insertionIndex(Performance p, int min, int max) {
        if (min == max) {
            return max;
        } else {
            int idx = (min + max) / 2;
            if (compare(data.get(idx), p) <= 0) {
                return insertionIndex(p, idx + 1, max);
            } else {
                return insertionIndex(p, min, idx);
            }
        }
    }
    
    private int compare(Performance a, Performance b) {
        if (a.getAuditorium() == b.getAuditorium()) {
            return a.getDateTime().compareTo(b.getDateTime());
        } else {
            return (a.getAuditorium() > b.getAuditorium()) ? 1 : -1;
        }
    }

    private GraphDataEvent scheduleDataAdded(Collection<Performance> add) {
        Collection<Performance> c = new ArrayList<>();
        for (Performance p : add) {
            if (add(p)) {
                c.add(p);
            }
        }
        return c.isEmpty() ? null : GraphDataEvent.newAddEvent(c);
    }
    
    private GraphDataEvent scheduleDataRemoved(Collection<Performance> rem) {
        Collection<Performance> c = new ArrayList<>();
        for (Performance p : rem) {
            if (remove(p)) {
                c.add(p);
            }
        }
        return c.isEmpty() ? null : GraphDataEvent.newRemoveEvent(c);
    }
    
    private GraphDataEvent scheduleDataReplaced(Collection<Performance> rem, Collection<Performance> add) {
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
        if (!crem.isEmpty() && !cadd.isEmpty()) {
            return GraphDataEvent.newReplaceEvent(crem, cadd);
        } else if (!crem.isEmpty()) {
            return GraphDataEvent.newRemoveEvent(crem);
        } else if (!cadd.isEmpty()) {
            return GraphDataEvent.newAddEvent(cadd);
        } else {
            return null;
        }
    }
}
