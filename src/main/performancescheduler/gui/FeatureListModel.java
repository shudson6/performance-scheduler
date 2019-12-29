package performancescheduler.gui;

import java.util.List;
import java.util.Objects;

import javax.swing.AbstractListModel;

import performancescheduler.core.FeatureManager;
import performancescheduler.data.Feature;

public class FeatureListModel extends AbstractListModel<Feature> {
	private static final long serialVersionUID = -6274475224180871020L;
	
	private FeatureManager featureManager;
	private List<Feature> data;
	
	public FeatureListModel(FeatureManager ftrMgr) {
		Objects.requireNonNull(ftrMgr);
		featureManager = ftrMgr;
		data = featureManager.getData();
	}

	@Override
	public int getSize() {
		return featureManager.size();
	}

	@Override
	public Feature getElementAt(int index) {
		return data.get(index);
	}
}
