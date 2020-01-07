package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import performancescheduler.core.PerformanceManager;
import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.gui.event.GraphDataListener;

@SuppressWarnings("serial")
public class PerformanceGraph extends JComponent {
    // TODO replace this with a context variable
    private static final Color gridLineColor = Color.DARK_GRAY;
    
    private PerformanceGraphModel model;
    private PerformanceManager manager;
    private PerformanceGraphCellRenderer renderer;
    private ListSelectionModel selectionModel = new DefaultListSelectionModel();
    
    private GraphDataListener listener = e -> repaint();
    
    public PerformanceGraph(PerformanceManager manager, PerformanceGraphModel model) {
        Objects.requireNonNull(manager);
        Objects.requireNonNull(model);
        this.manager = manager;
        setModel(model);
        renderer = new PerformanceGraphCellRenderer();
        setTransferHandler(new TransferHandler());
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    
    public int convertAudNumToCoordinateY(int audNum) {
        return (audNum - 1) * getAuditoriumHeight();
    }
    
    public LocalDateTime convertCoordinateXtoTime(int x) {
        return model.getRangeStart().plusMinutes(x / getPixelsPerMinute());
    }
    
    public int convertCoordinateYtoAudNum(int y) {
        return 1 + y / getAuditoriumHeight();
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
    
    public Performance getPerformanceAt(Point pt) {
        Performance result = null;
        // checks all performances instead of stopping when it finds one so that the topmost one will be returned
        for (Performance perf : model) {
            if (perf.getAuditorium() == convertCoordinateYtoAudNum(pt.y)
                    && renderer.getCellRendererComponent(this, perf, false, false).getBounds().contains(pt)) {
                result = perf;
            }
        }
        return result;
    }
    
    public int getPixelsPerMinute() {
        return 1;
    }
    
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        paintGrid(g);
        paintPerformances(g);
    }
    
    protected void paintGrid(Graphics g) {
        g.setColor(gridLineColor);
        for (int x = 0; x < getWidth(); x += 60 * getPixelsPerMinute()) {
            g.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += getAuditoriumHeight()) {
            g.drawLine(0, y, getWidth(), y);
        }
    }
    
    protected void paintPerformances(Graphics g) {
        model.forEach(p -> {
            Component c = renderer.getCellRendererComponent(this, p, false, false);
            c.paint(g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
        });
    }

    public final void setModel(PerformanceGraphModel model) {
        Objects.requireNonNull(model);
        if (this.model != null) {
            this.model.removeEventListener(listener);
        }
        this.model = model;
        this.model.addEventListener(listener);
        repaint();
    }
    
    public class TransferHandler extends javax.swing.TransferHandler implements DropTargetListener {
        public boolean canImport(TransferSupport ts) {
            return ts.isDataFlavorSupported(App.featureFlavor) || ts.isDataFlavorSupported(App.performanceFlavor);
        }
        
        public boolean importData(TransferSupport ts) {
            try {
                if (ts.isDataFlavorSupported(App.featureFlavor)) {
                    return importFeature(ts);
                } else if (ts.isDataFlavorSupported(App.performanceFlavor)) {
                    return importPerformances(ts);
                } else {
                    return false;
                }
            } catch (IOException | UnsupportedFlavorException ex) {
                return false;
            }
        }
        
        private boolean importFeature(TransferSupport ts) throws UnsupportedFlavorException, IOException {
            // TODO check drop action and implement different options (insta-create single, open dialog, etc)
            return manager.getModel().add(manager.getPerformanceFactory()
                    .createPerformance((Feature) ts.getTransferable().getTransferData(App.featureFlavor),
                            convertCoordinateXtoTime(ts.getDropLocation().getDropPoint().x),
                            convertCoordinateYtoAudNum(ts.getDropLocation().getDropPoint().y)));
        }
        
        private boolean importPerformances(TransferSupport ts) {
            // TODO need a selection model before this can be useful
            return false;
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {}

        @Override
        public void dragOver(DropTargetDragEvent dtde) {}

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {}

        @Override
        public void dragExit(DropTargetEvent dte) {}

        @Override
        public void drop(DropTargetDropEvent dtde) {}
        
        class PerformanceTransfer implements Iterable<Performance> {
            private final Collection<Performance> c;
            private final Point dragStart;
            
            public PerformanceTransfer(Collection<Performance> coll, Point start) {
                Objects.requireNonNull(coll);
                Objects.requireNonNull(start);
                c = new ArrayList<>(coll);
                dragStart = new Point(start);
            }
            
            public int getStartX() {
                return dragStart.x;
            }
            
            public int getStartY() {
                return dragStart.y;
            }
            
            @Override
            public Iterator<Performance> iterator() {
                return c.iterator();
            }
        }
    }
}
