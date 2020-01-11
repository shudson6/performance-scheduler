package performancescheduler.core;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;

public class PerformanceManager extends DataManager<Performance> {
	private final PerformanceFactory factory = PerformanceFactory.newFactory();

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
	
	public void movePerformances(Iterable<Performance> ip, int minutes, int auditoriums) {
	    Map<Performance, Performance> updateMap = new HashMap<>(model.size() * 2);
	    try {
		    for (Performance p : ip) {
		    	updateMap.put(p, adjustPerformance(p, minutes, auditoriums));
		    	// if this adjustment causes an out of range auditorium value, abandon the move
		    	if (updateMap.get(p).getAuditorium() > 14 || updateMap.get(p).getAuditorium() < 1) {
		    		return;
		    	}
		    }
		    model.update(updateMap);
	    } catch (Exception ex) {
	    	// we could get IllegalArgumentException if the adjustment makes an auditorium negative; this or
	    	// any other exception will cause us to simply abandon the move operation
	    	return;
	    }
	}
	
	private Performance adjustPerformance(Performance p, int m, int a) {
	    return factory.createPerformance(p.getFeature(), roundTo5min(p.getDateTime().plusMinutes(m)),
	    		p.getAuditorium() - a, p.getSeating(), p.getCleanup(), p.getTrailers());
	}
	
	public final LocalDateTime roundTo5min(LocalDateTime t) {
		int d = t.getMinute() % 5;
		if (d >= 3) {
			return t.plusMinutes(5 - d);
		} else {
			return t.minusMinutes(d);
		}
	}
}
