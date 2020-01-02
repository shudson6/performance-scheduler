package performancescheduler.gui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import performancescheduler.core.FeatureManager;
import performancescheduler.data.Feature;
import performancescheduler.data.storage.xml.XmlStorage;
import performancescheduler.gui.App;

public class ImportXmlAction extends AbstractAction {
	Component parent;
	FeatureManager ftrMgr;
	
	public ImportXmlAction(Component parent, FeatureManager ftrMgr) {
		Objects.requireNonNull(parent);
		Objects.requireNonNull(ftrMgr);
		putValue(Action.NAME, "Import...");
		this.parent = parent;
		this.ftrMgr = ftrMgr;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		if (jfc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			try {
				Collection<Feature> ftrs = new ArrayList<>(); 
				XmlStorage.getInstance(jfc.getSelectedFile()).restore(ftrs, null);
				ftrMgr.setData(ftrs);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(parent, "Failed to import data.");
			}
		}
	}

}
