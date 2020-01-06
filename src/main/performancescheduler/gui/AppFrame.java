package performancescheduler.gui;

import java.util.Objects;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import performancescheduler.data.Feature;

public class AppFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 5340974071886747788L;
	
	private final App app;
	private JSplitPane mainSplitPane;
	private JList<Feature> featureList;
	private JScrollPane ftrListPane;
	private PerformanceGraph pGraph;

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
		
		pGraph = new PerformanceGraph();
		pGraph.setModel(new PerformanceGraphModel(null, null));
		app.getPerformanceManager().addScheduleDataListener(pGraph.getModel());
		
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setDividerLocation(0.2d);
		mainSplitPane.setLeftComponent(featureList);
		mainSplitPane.setRightComponent(pGraph);
		
		setContentPane(mainSplitPane);
		
		pack();
	}
}
