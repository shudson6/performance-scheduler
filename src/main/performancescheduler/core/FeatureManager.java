package performancescheduler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import performancescheduler.core.event.EventFactory;
import performancescheduler.core.event.FeatureDataListener;
import performancescheduler.core.event.FeatureEvent;
import performancescheduler.data.Feature;

public class FeatureManager {
	private List<FeatureDataListener> listenerList;
	private EventFactory eventFactory = EventFactory.newFactory();
	private boolean eventsEnabled = true;
	
	private Collection<Feature> featureData = new TreeSet<>();
	
	public boolean add(Feature toAdd) {
		boolean result = featureData.add(toAdd);
		if (result) {
			fireAddFeature(toAdd);
		}
		return result;
	}
	
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
	
	public List<Feature> getData() {
		return new ArrayList<>(featureData);
	}
	
	public boolean remove(Feature toRemove) {
		boolean result = featureData.remove(toRemove);
		if (result) {
			fireRemoveFeature(toRemove);
		}
		return result;
	}
	
	public void removeFeatureDataListener(FeatureDataListener listener) {
		listenerList.remove(listener);
	}
	
	public void setData(Collection<Feature> data) {
		Objects.requireNonNull(data);
		Collection<Feature> old = featureData;
		featureData = new TreeSet<>(data);
		if (eventsEnabled) {
			fireEvent(eventFactory.newRemoveFeatureEvent(old));
			fireEvent(eventFactory.newAddFeatureEvent(data));
		}
	}
	
	public void setEventsEnabled(boolean enable) {
		eventsEnabled = enable;
	}
	
	public int size() {
		return featureData.size();
	}
	
	public boolean update(Feature before, Feature after) {
		if (featureData.remove(before)) {
			featureData.add(after);
			fireUpdateEvent(after, before);
			return true;
		}
		return false;
	}
}
