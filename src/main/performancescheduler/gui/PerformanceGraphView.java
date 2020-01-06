package performancescheduler.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Objects;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class PerformanceGraphView extends JComponent {
    // TODO replace this with a context variable
    private static final int PIXPERMIN = 1;
    private static final int AUDHEIGHT = 23;
    private static final int NUMAUDS = 14;
    private static final Color gridLineColor = Color.DARK_GRAY;
    
    private PerformanceGraphModel data;
    
    protected void paintGrid(Graphics g) {
        g.setColor(gridLineColor);
        for (int x = 0; x < getWidth(); x += 60 * PIXPERMIN) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += AUDHEIGHT) {
            g.drawLine(0, y, getWidth(), y);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        paintGrid(g);
    }
    
    public void setModel(PerformanceGraphModel model) {
        Objects.requireNonNull(model);
        data = model;
        repaint();
    }
}
