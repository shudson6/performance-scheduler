package performancescheduler.core;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;

public class FeatureManager extends DataManager<Feature> {
	private final FeatureFactory factory = FeatureFactory.newFactory();
	
	public FeatureManager(FeatureDataModel model) {
		super(model);
	}
	
	public FeatureFactory getFeatureFactory() {
		return factory;
	}
	
	@Override
	public FeatureDataModel getModel() {
		return (FeatureDataModel) super.getModel();
	}
}
