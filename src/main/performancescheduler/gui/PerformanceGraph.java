package performancescheduler.gui;

import java.awt.Graphics;
import java.util.Objects;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class PerformanceGraph extends JComponent {
    private PerformanceGraphModel data;
    
    @Override
    public void paintComponent(Graphics g) {
        
    }
    
    public void setModel(PerformanceGraphModel model) {
        Objects.requireNonNull(model);
        data = model;
        repaint();
    }
}
