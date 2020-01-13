package performancescheduler.gui.action;

import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.JOptionPane;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.gui.App;

@SuppressWarnings("serial")
public class RemoveFeatureAction extends PerformanceSchedulerAction {

    public RemoveFeatureAction(App a) {
        super("Remove Feature", a);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Feature f = app.getAppFrame().getFeatureList().getSelectedValue();
        if (f != null) {
            Collection<Performance> cp = app.getPerformanceManager()
                    .getPerformancesOf(app.getAppFrame().getFeatureList().getSelectedValue());
            if (!cp.isEmpty()) {
                if (JOptionPane.showConfirmDialog(app.getAppFrame(), 
                        "To remove the feature, " + cp.size() + " performances must also be removed. Continue?")
                        != JOptionPane.OK_OPTION) {
                    // abandon if they didn't click OK
                    return;
                }
            }
            app.getPerformanceManager().removePerformances(cp);
            app.getFeatureManager().removeFeature(f);
        } else {
            JOptionPane.showMessageDialog(app.getAppFrame(), "No feature is selected.");
        }
    }
}
