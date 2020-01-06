package performancescheduler.gui;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import performancescheduler.data.Feature;

@SuppressWarnings("serial")
public class FeatureTransferHandler extends TransferHandler {
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE | TransferHandler.LINK;
    }
    
    @Override
    public Transferable createTransferable(JComponent c) {
        return new FeatureTransferable((Feature) ((JList<?>) c).getSelectedValue());
    }
}
