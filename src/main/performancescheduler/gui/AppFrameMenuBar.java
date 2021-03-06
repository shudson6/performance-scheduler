package performancescheduler.gui;

import java.util.Objects;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import performancescheduler.gui.action.ExportXmlAction;
import performancescheduler.gui.action.ImportXmlAction;
import performancescheduler.gui.action.NewFeatureAction;
import performancescheduler.gui.action.RemoveFeatureAction;

@SuppressWarnings("serial")
public class AppFrameMenuBar extends javax.swing.JMenuBar {
	private App app;
	
	private JMenu fileMenu;
	private JMenuItem fileImport;
	private JMenuItem fileExport;
	
	private JMenu editMenu;
	private JMenuItem editUndo;
	private JMenuItem editRedo;
	private JMenuItem editNewFeature;
	private JMenuItem editRemoveFeature;
	
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
		editUndo = new JMenuItem(app.getUndoManager().getUndoAction());
		editMenu.add(editUndo);
		editRedo = new JMenuItem(app.getUndoManager().getRedoAction());
		editMenu.add(editRedo);
		editMenu.addSeparator();
		editNewFeature = new JMenuItem(new NewFeatureAction("New Feature", app));
		editMenu.add(editNewFeature);
		editRemoveFeature = new JMenuItem(new RemoveFeatureAction(app));
		editMenu.add(editRemoveFeature);
		return editMenu;
	}
}
