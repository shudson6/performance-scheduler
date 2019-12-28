package performancescheduler.core;

import java.util.List;

import performancescheduler.core.event.FeatureDataListener;
import performancescheduler.core.event.FeatureEvent;
import performancescheduler.data.Feature;

public class FeatureManager {
	private List<FeatureDataListener> listenerList;
	private boolean eventsEnabled = true;
	
	public void addFeatureDataListener(FeatureDataListener listener) {
		if (!listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}
	
	public boolean areEventsEnabled() {
		return eventsEnabled;
	}
	
	public void fireAddFeature(Feature added) {
		if (eventsEnabled) {
			fireEvent(FeatureEvent.newAddEvent(added));
		}
	}
	
	public void fireRemoveFeature(Feature removed) {
		if (eventsEnabled) {
			fireEvent(FeatureEvent.newRemoveEvent(removed));
		}
	}
	
	public void fireUpdateEvent(Feature updated, Feature pre) {
		if (eventsEnabled) {
			fireEvent(FeatureEvent.newReplaceEvent(updated, pre));
		}
	}
	
	public void fireEvent(FeatureEvent event) {
		listenerList.forEach(l -> l.featureDataChanged(event));
	}
	
	public void removeFeatureDataListener(FeatureDataListener listener) {
		listenerList.remove(listener);
	}
	
	public void setEventsEnabled(boolean enable) {
		eventsEnabled = enable;
	}
}
