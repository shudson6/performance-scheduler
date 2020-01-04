package performancescheduler.gui;

import java.util.Objects;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import performancescheduler.gui.action.ExportXmlAction;
import performancescheduler.gui.action.ImportXmlAction;
import performancescheduler.gui.action.NewFeatureAction;

@SuppressWarnings("serial")
public class AppFrameMenuBar extends javax.swing.JMenuBar {
	private App app;
	
	private JMenu fileMenu;
	private JMenuItem fileImport;
	private JMenuItem fileExport;
	
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
		fileImport = new JMenuItem(new ImportXmlAction("Import...", app));
		fileMenu.add(fileImport);
		fileExport = new JMenuItem(new ExportXmlAction("Export...", app));
		fileMenu.add(fileExport);
		return fileMenu;
	}
	
	private JMenu createEditMenu() {
		editMenu = new JMenu("Edit");
		editNewFeature = new JMenuItem(new NewFeatureAction("New Feature", app));
		editMenu.add(editNewFeature);
		return editMenu;
	}
}
