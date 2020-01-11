package performancescheduler.core;

import java.time.LocalDateTime;

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
	    ip.forEach(p -> model.update(p, movePerformance(p, minutes, auditoriums)));
	}
	
	private Performance movePerformance(Performance p, int m, int a) {
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
