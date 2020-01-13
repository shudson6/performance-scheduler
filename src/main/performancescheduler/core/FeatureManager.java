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
	
	public boolean addFeature(Feature f) {
	    boolean result = model.add(f);
	    if (result) {
	        fireUndoableEdit(new UndoableAddFeature(getModel(), f));
	    }
	    return result;
	}
	
	public boolean removeFeature(Feature f) {
	    boolean result = model.remove(f);
	    if (result) {
	        fireUndoableEdit(new UndoableRemoveFeature(getModel(), f));
	    }
	    return result;
	}
}
