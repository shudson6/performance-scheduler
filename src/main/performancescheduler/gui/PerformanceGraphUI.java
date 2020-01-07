package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.Duration;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class PerformanceGraphUI extends ComponentUI {
    // TODO replace this with a context variable
    private static final Color gridLineColor = Color.DARK_GRAY;
    
    private PerformanceGraph graph;
    private final Handler handler = new Handler();
    
	@Override
	public void installUI(JComponent c) {
		if (c instanceof PerformanceGraph) {
			graph = (PerformanceGraph) c;
			graph.setOpaque(true);
			graph.setBackground(Color.LIGHT_GRAY);
			graph.addMouseListener(handler);
		} else {
			throw new IllegalArgumentException("PerformanceGraphUI may only be installed on a PerformanceGraph.");
		}
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		if (c.equals(graph)) {
			graph = null;
		}
	}
	
	public Dimension getPreferredSize(JComponent c) {
		return new Dimension(graph.getPixelsPerMinute()
				* (int) Duration.between(graph.getModel().getRangeStart(), graph.getModel().getRangeEnd()).toMinutes(),
				graph.getAuditoriumHeight() * 14);
	}
    
    @Override
    public void paint(Graphics g, JComponent c) {
        paintGrid(g);
        paintPerformances(g);
    }
    
    protected void paintGrid(Graphics g) {
        g.setColor(gridLineColor);
        for (int x = 0; x < graph.getWidth(); x += 60 * graph.getPixelsPerMinute()) {
            g.drawLine(x, 0, x, graph.getHeight());
        }
        for (int y = 0; y < graph.getHeight(); y += graph.getAuditoriumHeight()) {
            g.drawLine(0, y, graph.getWidth(), y);
        }
    }
    
    protected void paintPerformances(Graphics g) {
    	for (int i = 0; i < graph.getModel().size(); i++) {
    		Component c = graph.getPerformanceRenderer().getCellRendererComponent(
    				graph, graph.getModel().getElementAt(i),
    				graph.getSelectionModel().isSelectedIndex(i), false);
    		c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
    	}
//        graph.getModel().forEach(p -> {
//            Component c = graph.getPerformanceRenderer().getCellRendererComponent(graph, p, false, false);
//            c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
//        });
    }
    
    private class Handler implements MouseListener, KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int index = graph.pointToIndex(e.getPoint());
			graph.getSelectionModel().setSelectionInterval(index, index);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
    }
}
