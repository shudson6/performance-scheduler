package performancescheduler.gui.action;

import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;

import performancescheduler.gui.App;

@SuppressWarnings("serial")
public abstract class PerformanceSchedulerAction extends AbstractAction {
    protected final App app;
    
    public PerformanceSchedulerAction(String name, App a) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(a);
        
        app = a;
        putValue(Action.NAME, name);
    }
}
