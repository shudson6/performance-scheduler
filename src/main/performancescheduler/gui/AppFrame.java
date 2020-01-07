package performancescheduler.gui;

import java.util.Objects;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import performancescheduler.data.Feature;

@SuppressWarnings("serial")
public class AppFrame extends javax.swing.JFrame {
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
		
		featureList = new JList<>(new FeatureListModel(app.getFeatureManager().getModel()));
		featureList.setCellRenderer(new FeaturePanelCellRenderer());
		featureList.setTransferHandler(new FeatureTransferHandler());
		featureList.setDragEnabled(true);
		ftrListPane = new JScrollPane(featureList);
		
		pGraph = new PerformanceGraph(app);
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
