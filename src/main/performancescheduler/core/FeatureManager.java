package performancescheduler.core;

import java.util.Arrays;
import java.util.List;

import performancescheduler.core.event.EventFactory;
import performancescheduler.core.event.FeatureDataListener;
import performancescheduler.core.event.FeatureEvent;
import performancescheduler.data.Feature;

public class FeatureManager {
	private List<FeatureDataListener> listenerList;
	private EventFactory eventFactory = EventFactory.newFactory();
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
			fireEvent(eventFactory.newAddFeatureEvent(Arrays.asList(added)));
		}
	}
	
	public void fireRemoveFeature(Feature removed) {
		if (eventsEnabled) {
			fireEvent(eventFactory.newRemoveFeatureEvent(Arrays.asList(removed)));
		}
	}
	
	public void fireUpdateEvent(Feature updated, Feature pre) {
		if (eventsEnabled) {
			fireEvent(eventFactory.newUpdateFeatureEvent(Arrays.asList(updated), Arrays.asList(pre)));
		}
	}
	
	public void fireEvent(FeatureEvent event) {
	    if (eventsEnabled) {
	        listenerList.forEach(l -> l.featureDataChanged(event));
	    }
	}
	
	public void removeFeatureDataListener(FeatureDataListener listener) {
		listenerList.remove(listener);
	}
	
	public void setEventsEnabled(boolean enable) {
		eventsEnabled = enable;
	}
}
