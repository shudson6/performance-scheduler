package performancescheduler.gui;

import java.util.Objects;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import performancescheduler.gui.action.ImportXmlAction;
import performancescheduler.gui.action.NewFeatureAction;

public class AppFrameMenuBar extends javax.swing.JMenuBar {
	private App app;
	
	private JMenu fileMenu;
	private JMenuItem fileImport;
	
	private JMenu editMenu;
	private JMenuItem editNewFeature;
	
	public AppFrameMenuBar(App a) {
		super();
		Objects.requireNonNull(a);
		app = a;
		init();
	}
	
	private void init() {
		add(createFileMenu());
		add(createEditMenu());
	}
	
	private JMenu createFileMenu() {
		fileMenu = new JMenu("File");
		fileImport = new JMenuItem(new ImportXmlAction(this, App.getInstance().getFeatureManager()));
		fileImport.getAction().putValue(Action.NAME, "Import...");
		fileMenu.add(fileImport);
		return fileMenu;
	}
	
	private JMenu createEditMenu() {
		editMenu = new JMenu("Edit");
		editNewFeature = new JMenuItem(new NewFeatureAction(app.getAppFrame(), app.getFeatureManager()));
		editNewFeature.getAction().putValue(Action.NAME, "New Feature");
		editMenu.add(editNewFeature);
		return editMenu;
	}
}
