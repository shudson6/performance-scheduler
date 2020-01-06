package performancescheduler.gui;

import javax.swing.SwingUtilities;

import performancescheduler.core.FeatureManager;
import performancescheduler.core.PerformanceManager;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;

public class App {
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
	private FeatureManager ftrMgr;
	private PerformanceManager pfmMgr;
	
	public AppFrame getAppFrame() {
		return appFrame;
	}
	
	public FeatureManager getFeatureManager() {
		return ftrMgr;
	}
	
	public PerformanceManager getPerformanceManager() {
	    return pfmMgr;
	}
	
	protected void start() {
		ftrMgr = new FeatureManager();
		pfmMgr = new PerformanceManager();
		
		appFrame = new AppFrame(this);
		appFrame.setVisible(true);
	}
}
