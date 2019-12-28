package performancescheduler.core.event;

import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Feature;

public class FeatureEvent extends ScheduleEvent<Feature> {
	public static FeatureEvent newAddEvent(Feature p) {
		Objects.requireNonNull(p, "FeatureEvent.newAddEvent: nothing to add.");
		return new FeatureEvent(wrapObject(p), null, ADD);
	}
	
	public static FeatureEvent newAddEvent(Collection<Feature> p) {
		Objects.requireNonNull(p, "FeatureEvent.newAddEvent: collection must not be null.");
		return new FeatureEvent(p, null, ADD);
	}
	
	public static FeatureEvent newRemoveEvent(Feature p) {
		Objects.requireNonNull(p, "FeatureEvent.newRemoveEvent: nothing to remove.");
		return new FeatureEvent(null, wrapObject(p), REMOVE);
	}
	
	public static FeatureEvent newRemoveEvent(Collection<Feature> p) {
		Objects.requireNonNull(p, "FeatureEvent.newRemoveEvent: collection must not be null.");
		return new FeatureEvent(null, p, REMOVE);
	}
	
	public static FeatureEvent newReplaceEvent(Feature add, Feature rem) {
		Objects.requireNonNull(add, "FeatureEvent.newReplaceEvent: add must not be null.");
		Objects.requireNonNull(rem, "FeatureEvent.newReplaceEvent: rem must not be null.");
		return new FeatureEvent(wrapObject(add), wrapObject(rem), REPLACE);
	}
	
	public static FeatureEvent newReplaceEvent(Collection<Feature> add, Collection<Feature> rem) {
		Objects.requireNonNull(add, "FeatureEvent.newReplaceEvent: add must not be null.");
		Objects.requireNonNull(rem, "FeatureEvent.newReplaceEvent: rem must not be null.");
		return new FeatureEvent(add, rem, REPLACE);
	}
	
	private FeatureEvent(Collection<Feature> add, Collection<Feature> rem, int action) {
		super(add, rem, action);
	}
}
