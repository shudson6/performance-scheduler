package performancescheduler.gui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.storage.xml.XmlStorage;
import performancescheduler.gui.App;

@SuppressWarnings("serial")
public class ImportXmlAction extends PerformanceSchedulerAction {
	public ImportXmlAction(String name, App app) {
	    super(name, app);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		if (jfc.showOpenDialog(app.getAppFrame()) == JFileChooser.APPROVE_OPTION) {
			try {
				Collection<Feature> ftrs = new ArrayList<>(); 
				Collection<Performance> pfms = new ArrayList<>();
				XmlStorage.getInstance(jfc.getSelectedFile()).restore(ftrs, pfms);
				app.getFeatureManager().getModel().setData(ftrs);
				app.getPerformanceManager().setData(pfms);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(app.getAppFrame(), "Failed to import data.");
			}
		}
	}
}
