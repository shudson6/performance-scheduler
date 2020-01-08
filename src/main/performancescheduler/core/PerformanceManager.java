package performancescheduler.core;

import java.util.Collection;

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
	    System.out.format("Changes: minutes %d, auds %d%n", m, a);
	    return factory.createPerformance(p.getFeature(), p.getDateTime().plusMinutes(m), p.getAuditorium() - a);
	}
}
