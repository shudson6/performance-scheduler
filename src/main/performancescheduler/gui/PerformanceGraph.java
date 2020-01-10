package performancescheduler.gui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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
        selectionModel.addListSelectionListener(e -> repaint());
        setUI(new PerformanceGraphUI());
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
    
    public PerformanceGraphCellRenderer getPerformanceCellRenderer() {
    	return renderer;
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
    
    public Collection<Performance> getSelectedPerformances() {
        Collection<Performance> c = new ArrayList<>();
        for (int i : selectionModel.getSelectedIndices()) {
            c.add(model.getElementAt(i));
        }
        return c;
    }
    
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }
    
    @Override
    public String getUIClassID() {
    	return "performanceGraphUI";
    }
    
    public int pointToIndex(Point p) {
    	return model.indexOf(getPerformanceAt(p));
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
    
    @Override
    public TransferHandler getTransferHandler() {
        return (TransferHandler) super.getTransferHandler();
    }
    
    public class TransferHandler extends javax.swing.TransferHandler implements DropTargetListener {
        private Point xferStartPoint;
        
        public void setXferStartPoint(Point p) {
            xferStartPoint = p;
        }
//
//	Commented out until Drag Image is a current task
//
        
//        @Override
//        public void exportAsDrag(JComponent comp, InputEvent e, int action) {
//            setDragImage(new DragImage(getSelectedPerformances().iterator().next()));
//            super.exportAsDrag(comp, e, action);
//        }
        
        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }
        
        @Override
        public Transferable createTransferable(JComponent c) {
            return new PerformanceTransfer(getSelectedPerformances(), xferStartPoint);
        }
        
        @Override
        public boolean canImport(TransferSupport ts) {
            return ts.isDataFlavorSupported(App.featureFlavor) || ts.isDataFlavorSupported(App.performanceFlavor);
        }
        
        @Override
        public boolean importData(TransferSupport ts) {
            try {
                if (ts.isDataFlavorSupported(App.featureFlavor)) {
                    return importFeature(ts);
                } else if (ts.isDataFlavorSupported(App.performanceFlavor)) {
                    return importPerformances(ts);
                } else {
                    System.out.println("Not supported");
                    return false;
                }
            } catch (IOException | UnsupportedFlavorException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        
        private boolean importFeature(TransferSupport ts) throws UnsupportedFlavorException, IOException {
            // TODO check drop action and implement different options (insta-create single, open dialog, etc)
            return manager.getModel().add(manager.getPerformanceFactory()
                    .createPerformance((Feature) ts.getTransferable().getTransferData(App.featureFlavor),
                            convertCoordinateXtoTime(ts.getDropLocation().getDropPoint().x),
                            convertCoordinateYtoAudNum(ts.getDropLocation().getDropPoint().y),
                            0, 25, 18));
        }
        
        @SuppressWarnings("unchecked")
        private boolean importPerformances(TransferSupport ts) {
            try {
                manager.movePerformances(
                        (Collection<Performance>) ts.getTransferable().getTransferData(App.performanceFlavor),
                        timeChangeMinutes(ts.getDropLocation().getDropPoint(), xferStartPoint),
                        auditoriumChange(ts.getDropLocation().getDropPoint(), xferStartPoint));
                return true;
            } catch (UnsupportedFlavorException | IOException | ClassCastException ex) {
                return false;
            }
        }
        
        private int auditoriumChange(Point a, Point b) {
            return convertCoordinateYtoAudNum(b.y) - convertCoordinateYtoAudNum(a.y);
        }
        
        private int timeChangeMinutes(Point a, Point b) {
            return (int) Duration.between(convertCoordinateXtoTime(b.x), convertCoordinateXtoTime(a.x)).toMinutes();
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
        
        class PerformanceTransfer implements Transferable {
            private final Collection<Performance> c;
            private final Point dragStart;
            
            public PerformanceTransfer(Collection<Performance> coll, Point start) {
                Objects.requireNonNull(coll);
                Objects.requireNonNull(start);
                c = new ArrayList<>(coll);
                dragStart = new Point(start);
            }
            
            public final Point getStartPoint() {
                return dragStart;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] {App.performanceFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(App.performanceFlavor);
            }

            @Override
            public Collection<Performance> getTransferData(DataFlavor flavor) 
                    throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor)) {
                    return c;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
        }
//
//	Commented out until Drag Image is a current task
//
        
//        class DragImage extends BufferedImage {
//            public DragImage(Performance p) {
//                super(getPerformanceCellRenderer().getCellRendererComponent(PerformanceGraph.this, p, false, false)
//                        .getWidth(),
//                        getPerformanceCellRenderer().getCellRendererComponent(PerformanceGraph.this, p, false, false)
//                        .getHeight(),
//                        BufferedImage.TYPE_INT_ARGB);
//                drawImage(p);
//            }
//            
//            private void drawImage(Performance p) {
//                getPerformanceCellRenderer().getCellRendererComponent(PerformanceGraph.this, p, false, false)
//                        .paint(this.createGraphics());
//            }
//        }
    }
}
