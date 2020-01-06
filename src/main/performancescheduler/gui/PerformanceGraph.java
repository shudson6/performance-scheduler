package performancescheduler.gui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class PerformanceGraph extends JScrollPane {
    
    private PerformanceGraphModel model;
    
    public PerformanceGraph() {
        setViewportView(new PerformanceGraphView(this));
    }
    
    public int convertAudNumToCoordinateY(int audNum) {
        return (audNum - 1) * getAuditoriumHeight();
    }
    
    public int convertTimeToCoordinateX(LocalDateTime time) {
        return getPixelsPerMinute() * (int) Duration.between(model.getRangeStart(), time).toMinutes();
    }
    
    public int getAuditoriumHeight() {
        return 43;
    }
    
    public PerformanceGraphModel getModel() {
        return model;
    }
    
    public int getPixelsPerMinute() {
        return 1;
    }

    public void setModel(PerformanceGraphModel model) {
        Objects.requireNonNull(model);
        this.model = model;
        repaint();
    }
}
