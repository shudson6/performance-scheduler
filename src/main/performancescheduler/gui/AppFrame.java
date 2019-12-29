package performancescheduler.gui;

import javax.swing.JList;

import performancescheduler.core.FeatureManager;
import performancescheduler.data.Feature;

public class AppFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 5340974071886747788L;

	public AppFrame() {
		super("Woot");
		init();
	}
	
	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(new JList<Feature>(new FeatureListModel(new FeatureManager())));
	}
}
