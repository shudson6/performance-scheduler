package performancescheduler.gui;

import javax.swing.SwingUtilities;

import performancescheduler.core.FeatureManager;
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
	
	public FeatureManager getFeatureManager() {
		return ftrMgr;
	}
	
	protected void start() {
		ftrMgr = new FeatureManager();
		
		// TODO: remove this 'test' code
		FeatureFactory ftrFactory = FeatureFactory.newFactory();
		ftrMgr.add(ftrFactory.createFeature("Foobar", Rating.R, 105, false, true, true, true));
		ftrMgr.add(ftrFactory.createFeature("Hungry", Rating.PG, 90, false, true, true, true));
		
		appFrame = new AppFrame(this);
		appFrame.setVisible(true);
		
	}
}
