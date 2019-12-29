package performancescheduler.gui;

import javax.swing.SwingUtilities;

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
	
	protected void start() {
		appFrame = new AppFrame();
		appFrame.setVisible(true);
	}
}
