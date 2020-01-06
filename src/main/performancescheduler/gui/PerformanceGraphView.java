package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Objects;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class PerformanceGraphView extends JComponent {
    // TODO replace this with a context variable
    private static final Color gridLineColor = Color.DARK_GRAY;
    
    private final PerformanceGraph graph;
    private PerformanceGraphCellRenderer renderer;
    
    public PerformanceGraphView(PerformanceGraph parent) {
        Objects.requireNonNull(parent);
        graph = parent;
        renderer = new PerformanceGraphCellRenderer();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        paintGrid(g);
        paintPerformances(g);
    }
    
    protected void paintGrid(Graphics g) {
        g.setColor(gridLineColor);
        for (int x = 0; x < getWidth(); x += 60 * graph.getPixelsPerMinute()) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += graph.getAuditoriumHeight()) {
            g.drawLine(0, y, getWidth(), y);
        }
    }
    
    protected void paintPerformances(Graphics g) {
        graph.getModel().forEach(p -> {
            Component c = renderer.getCellRendererComponent(graph, p, false, false);
            c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
        });
    }
}
