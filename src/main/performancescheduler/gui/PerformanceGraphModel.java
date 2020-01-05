package performancescheduler.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.data.Performance;
import performancescheduler.gui.event.GraphModelListener;

public class PerformanceGraphModel implements ScheduleDataListener<Performance> {
    private List<GraphModelListener> listenerList = new ArrayList<>();
    private Collection<Performance> data;
    
    public void add(Performance p) {
        if (data.add(p)) {
            fireAddEvent(p);
        }
    }
    
    public void fireAddEvent(Performance p) {
        
    }
    
    public void fireRemoveEvent(Performance p) {
        
    }
    
    public void fireReplaceEvent(Performance old, Performance add) {
        
    }
    
    public void remove(Performance p) {
        if (data.remove(p)) {
            fireRemoveEvent(p);
        }
    }
    
    public void replace(Performance old, Performance add) {
        // TODO should an exception be thrown if old isn't in the list? NoSuchElementException perhaps?
        // TODO should this also be applied to ScheduleDataManager?
    }

    @Override
    public void scheduleDataChanged(ScheduleEvent<Performance> event) {
        // TODO Auto-generated method stub
        
    }

}
