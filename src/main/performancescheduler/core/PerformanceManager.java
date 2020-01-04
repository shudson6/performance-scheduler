package performancescheduler.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import performancescheduler.core.event.PerformanceDataListener;
import performancescheduler.data.Performance;

public class PerformanceManager {
    private List<PerformanceDataListener> listenerList = new ArrayList<>();
    private boolean eventsEnabled = true;
    
    private	Collection<Performance> performanceData = new ArrayList<>();
	
	public boolean add(Performance performance) {
		if (performance != null && performanceData.add(performance)) {
			// fire event
			return true;
		}
		return false;
	}
	
	public boolean add(Collection<Performance> performances) {
		if (performances != null && performanceData.addAll(performances)) {
			// fire event (likely need to keep up with which elements were really added
			return true;
		}
		return false;
	}
	
	public boolean remove(Performance performance) {
		if (performance != null && performanceData.remove(performance)) {
			// fire event
			return true;
		}
		return false;
	}
	
	public boolean remove(Collection<Performance> performances) {
		if (performances != null && performanceData.removeAll(performances)) {
			// fire event (keep up with which elements are removed
			return true;
		}
		return false;
	}
	
	public boolean replace(Performance toRemove, Performance toAdd) {
		if (performanceData.contains(toRemove)) {
			performanceData.remove(toRemove);
			performanceData.add(toAdd);
			// fire event
			return true;
		}
		return false;
	}
	
	public boolean replace(Collection<Performance> toRemove, Collection<Performance> toAdd) {
		return false;
	}
}
