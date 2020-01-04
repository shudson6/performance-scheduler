package performancescheduler.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import performancescheduler.data.FeatureFactory;
import performancescheduler.gui.App;
import performancescheduler.gui.dialog.FeatureDialog;

@SuppressWarnings("serial")
public class NewFeatureAction extends PerformanceSchedulerAction {
	public NewFeatureAction(String name, App app) {
	    super(name, app);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FeatureDialog dialog = new FeatureDialog(app.getAppFrame(), FeatureFactory.newFactory());
		if (dialog.showNewFeatureDialog()) {
			if (!app.getFeatureManager().add(dialog.getCreatedFeature())) {
				JOptionPane.showMessageDialog(app.getAppFrame(), "Could not add feature to the document.");
			}
		}
	}
}