package performancescheduler.gui;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

import performancescheduler.data.Feature;
import performancescheduler.gui.event.GraphDataListener;

@SuppressWarnings("serial")
public class PerformanceGraph extends JScrollPane {
    private final App app;
    private PerformanceGraphModel model;
    private PerformanceGraphView view;
    private GraphDataListener listener = e -> view.repaint();
    
    public PerformanceGraph(final App app) {
        Objects.requireNonNull(app);
        this.app = app;
        view = new PerformanceGraphView(this);
        view.setTransferHandler(new PGTransferHandler());
        setViewportView(view);
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
    
    public int getPixelsPerMinute() {
        return 1;
    }

    public void setModel(PerformanceGraphModel model) {
        Objects.requireNonNull(model);
        if (this.model != null) {
            this.model.removeEventListener(listener);
        }
        this.model = model;
        this.model.addEventListener(listener);
        repaint();
    }
    
    public class PGTransferHandler extends TransferHandler implements DropTargetListener {
        public boolean canImport(TransferSupport ts) {
            return ts.isDataFlavorSupported(App.featureFlavor);
        }
        
        public boolean importData(TransferSupport ts) {
            if (canImport(ts)) {
                try {
                    app.getPerformanceManager().getModel().add(app.getPerformanceManager().getPerformanceFactory()
                            .createPerformance((Feature) ts.getTransferable().getTransferData(App.featureFlavor),
                                    convertCoordinateXtoTime(ts.getDropLocation().getDropPoint().x),
                                    convertCoordinateYtoAudNum(ts.getDropLocation().getDropPoint().y)));
                    return true;
                } catch (UnsupportedFlavorException | IOException e) {
                    return false;
                }
            }
            return false;
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            // TODO Auto-generated method stub
            
        }
        
    }
}
