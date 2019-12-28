package performancescheduler.core.event;

@FunctionalInterface
public interface FeatureDataListener {
	public void featureDataChanged(FeatureEvent event);
}
