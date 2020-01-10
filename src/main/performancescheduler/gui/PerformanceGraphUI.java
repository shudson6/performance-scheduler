package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.time.Duration;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
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
			graph.addMouseMotionListener(handler);
			graph.addKeyListener(handler);
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
    		paintPerformanceComponent(g, 
    		        graph.getPerformanceCellRenderer().getCellRendererComponent(graph, graph.getModel().getElementAt(i),
    		                graph.getSelectionModel().isSelectedIndex(i), false));
    	}
    }
    
    private void paintPerformanceComponent(Graphics g, Component c) {
		c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
    }
    
    private class Handler implements MouseListener, MouseMotionListener, KeyListener {
        private Point lastLeftPress;

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		    // save the location of a left-press; we will want it if a drag starts
		    if (e.getButton() == MouseEvent.BUTTON1) {
		        lastLeftPress = e.getPoint();
		    }
		    maybeUpdateSelection(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		    // ask for focus so we can receive key events
			if (e.getSource() instanceof JComponent) {
			    ((JComponent) e.getSource()).requestFocusInWindow();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
		private void maybeUpdateSelection(MouseEvent e) {
		    int index = graph.pointToIndex(e.getPoint());
		    if (index < 0) {
		        // clear selection when click is in space
		        graph.getSelectionModel().clearSelection();
		    } else {
		        // update selection according to modifier keys
		        if (e.isControlDown()) {
		            // control-click: invert selection status of the index
		            if (graph.getSelectionModel().isSelectedIndex(index)) {
		                graph.getSelectionModel().removeSelectionInterval(index, index);
		            } else {
		                graph.getSelectionModel().addSelectionInterval(index, index);
		            }
		        } else if (e.isShiftDown()) {
		            // shift-click: add everything between the last two clicks
		            graph.getSelectionModel().addSelectionInterval(graph.getSelectionModel().getLeadSelectionIndex(),
		                    index);
		        } else {
		            // no modifier: single-select; do nothing if already selected, so not to disturb the selection
		            if (!graph.getSelectionModel().isSelectedIndex(index)) {
		                graph.getSelectionModel().setSelectionInterval(index, index);
		            }
		        }
		    }
		}

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!graph.getSelectionModel().isSelectionEmpty()) {
                graph.getTransferHandler().setXferStartPoint(lastLeftPress);
                graph.getTransferHandler().exportAsDrag(graph, e, TransferHandler.COPY);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}
