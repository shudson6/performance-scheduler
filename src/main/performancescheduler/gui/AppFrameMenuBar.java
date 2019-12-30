package performancescheduler.gui;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import performancescheduler.gui.action.ImportXmlAction;

public class AppFrameMenuBar extends javax.swing.JMenuBar {
	private JMenu fileMenu;
	private JMenuItem fileImport;
	
	public AppFrameMenuBar() {
		super();
		init();
	}
	
	private void init() {
		add(createFileMenu());
	}
	
	private JMenu createFileMenu() {
		fileMenu = new JMenu("File");
		fileImport = new JMenuItem(new ImportXmlAction(this, App.getInstance().getFeatureManager()));
		fileImport.getAction().putValue(Action.NAME, "Import...");
		fileMenu.add(fileImport);
		return fileMenu;
	}
}
