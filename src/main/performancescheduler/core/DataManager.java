package performancescheduler.core;

import java.util.Objects;

public abstract class DataManager<T> {
	protected final ScheduleDataModel<T> model;
	
	public DataManager(ScheduleDataModel<T> model) {
		Objects.requireNonNull(model);
		this.model = model;
	}
	
	public ScheduleDataModel<T> getModel() {
		return model;
	}
}
