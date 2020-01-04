package performancescheduler.gui;

import java.util.Objects;

import javax.swing.JList;
import javax.swing.JScrollPane;

import performancescheduler.data.Feature;

public class AppFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 5340974071886747788L;
	
	private final App app;
	private JList<Feature> featureList;
	private JScrollPane ftrListPane;

	public AppFrame(final App app) {
		super("Woot");
		Objects.requireNonNull(app);
		this.app = app;
		init();
	}
	
	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(new AppFrameMenuBar(app));
		
		featureList = new JList<>(new FeatureListModel(app.getFeatureManager()));
		featureList.setCellRenderer(new FeaturePanelCellRenderer());
		ftrListPane = new JScrollPane(featureList);
		setContentPane(ftrListPane);
		
		pack();
	}
}
