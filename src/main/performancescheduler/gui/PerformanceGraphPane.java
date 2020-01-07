package performancescheduler.gui;

import java.util.Objects;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class PerformanceGraphPane extends JScrollPane {
    
    public PerformanceGraphPane(PerformanceGraph graphView) {
        Objects.requireNonNull(graphView);
        setViewportView(graphView);
    }
}
