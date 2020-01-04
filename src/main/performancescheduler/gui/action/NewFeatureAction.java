package performancescheduler.gui.action;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import performancescheduler.core.FeatureManager;
import performancescheduler.data.FeatureFactory;
import performancescheduler.gui.dialog.FeatureDialog;

public class NewFeatureAction extends AbstractAction {
	Frame parent;
	FeatureManager featureManager;
	
	public NewFeatureAction(Frame parent, FeatureManager featureManager) {
		this.parent = parent;
		this.featureManager = featureManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FeatureDialog dialog = new FeatureDialog(parent, FeatureFactory.newFactory());
		if (dialog.showNewFeatureDialog()) {
			if (!featureManager.add(dialog.getCreatedFeature())) {
				JOptionPane.showMessageDialog(parent, "Could not add feature to the document.");
			}
		}
	}
}
