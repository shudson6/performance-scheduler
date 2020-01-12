package performancescheduler.gui;

import java.awt.datatransfer.DataFlavor;

import javax.swing.SwingUtilities;

import performancescheduler.core.FeatureDataModel;
import performancescheduler.core.FeatureManager;
import performancescheduler.core.PerformanceDataModel;
import performancescheduler.core.PerformanceManager;

public class App {
    public static final DataFlavor featureFlavor = createFlavor("performancescheduler.data.Feature");
    public static final DataFlavor performanceFlavor = createFlavor("performancescheduler.data.Performance");
    private static DataFlavor createFlavor(String s) {
        try {
            return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + s + "\"");
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
	private static App app = null;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> getInstance().start());
	}
	
	protected static App getInstance() {
		if (app == null) {
			app = new App();
		}
		return app;
	}
	
	private AppFrame appFrame;
	private FeatureDataModel featureModel;
	private FeatureManager featureManager;
	private PerformanceDataModel performModel;
	private PerformanceManager performManager;
	private UndoManager undoManager;
	
	public AppFrame getAppFrame() {
		return appFrame;
	}
	
	public FeatureManager getFeatureManager() {
		return featureManager;
	}
	
	public PerformanceManager getPerformanceManager() {
	    return performManager;
	}
	
	public UndoManager getUndoManager() {
	    return undoManager;
	}
	
	protected void start() {
	    undoManager = new UndoManager();
		featureModel = new FeatureDataModel();
		featureManager = new FeatureManager(featureModel);
		featureManager.addUndoableEditListener(undoManager);
		performModel = new PerformanceDataModel();
		performManager = new PerformanceManager(performModel);
		performManager.addUndoableEditListener(undoManager);
		
		appFrame = new AppFrame(this);
		appFrame.setVisible(true);
	}
}
