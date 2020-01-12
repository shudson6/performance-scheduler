package performancescheduler.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;

public class PerformanceManager extends DataManager<Performance> {
	private final PerformanceFactory factory = PerformanceFactory.newFactory();
	private final int auditoriumCount = 14;

	public PerformanceManager(PerformanceDataModel model) {
		super(model);
	}

	public PerformanceFactory getPerformanceFactory() {
		return factory;
	}
	
	@Override
	public PerformanceDataModel getModel() {
		return (PerformanceDataModel) super.getModel();
	}
	
	public int getAuditoriumCount() {
	    return auditoriumCount;
	}
	
	public void addPerformance(Performance p) {
		addPerformances(Arrays.asList(p));
	}
	
	public void addPerformances(Collection<Performance> cp) {
		Collection<Performance> toAdd = new ArrayList<>(cp.size());
		for (Performance p : cp) {
			if (p.getAuditorium() <= auditoriumCount && p.getAuditorium() >= 1) {
				toAdd.add(roundTo5min(p));
			} else {
				// abandon the operation if an auditorium is out of range
				return;
			}
		}
		model.add(toAdd);
		fireUndoableEdit(new UndoableAddPerformances(getModel(), toAdd));
	}
	
	public void removePerformances(Collection<Performance> cp) {
	    Collection<Performance> removed = new ArrayList<>(cp);
	    model.remove(removed);
	    fireUndoableEdit(new UndoableRemovePerformance(getModel(), removed));
	}
	
	public void movePerformances(Collection<Performance> ip, int minutes, int auditoriums) {
	    if (ip == null || ip.isEmpty()) {
	        return;
	    }
	    Map<Performance, Performance> updateMap = new HashMap<>(model.size() * 2);
	    for (Performance p : ip) {
	    	// if this adjustment causes an out of range auditorium value, abandon the move
	        if (p.getAuditorium() + auditoriums < 1 || p.getAuditorium() + auditoriums > auditoriumCount) {
	            return;
	        }
	    	updateMap.put(p, adjustPerformance(p, minutes, auditoriums));
	    	if (updateMap.get(p).getAuditorium() > auditoriumCount || updateMap.get(p).getAuditorium() < 1) {
	    		return;
	    	}
	    }
	    model.update(updateMap);
	    fireUndoableEdit(new UndoableMovePerformances(getModel(), updateMap));
	}
	
	private Performance adjustPerformance(Performance p, int m, int a) {
	    return factory.createPerformance(p.getFeature(), roundTo5min(p.getDateTime().plusMinutes(m)),
	    		p.getAuditorium() + a, p.getSeating(), p.getCleanup(), p.getTrailers());
	}
	
	public final LocalDateTime roundTo5min(LocalDateTime t) {
		int d = t.getMinute() % 5;
		if (d >= 3) {
			return t.plusMinutes(5 - d);
		} else {
			return t.minusMinutes(d);
		}
	}
	
	public final Performance roundTo5min(Performance p) {
		if (p.getDateTime().getMinute() % 5 == 0) {
			return p;
		} else {
			return factory.createPerformance(p.getFeature(), roundTo5min(p.getDateTime()), p.getAuditorium(),
					p.getSeating(), p.getCleanup(), p.getTrailers());
		}
	}
}
