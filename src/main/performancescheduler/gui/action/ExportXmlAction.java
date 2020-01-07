package performancescheduler.gui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import performancescheduler.data.storage.xml.XmlStorage;
import performancescheduler.gui.App;

@SuppressWarnings("serial")
public class ExportXmlAction extends PerformanceSchedulerAction {
    public ExportXmlAction(String name, App app) {
        super(name, app);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
        if (jfc.showSaveDialog(app.getAppFrame()) == JFileChooser.APPROVE_OPTION) {
            try {
                XmlStorage.getInstance(jfc.getSelectedFile()).store(app.getFeatureManager().getModel().getData(), 
                        app.getPerformanceManager().getModel().getData());
                JOptionPane.showMessageDialog(app.getAppFrame(), "Export complete.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(app.getAppFrame(), "Failed to export data.");
            }
        }
    }
}
