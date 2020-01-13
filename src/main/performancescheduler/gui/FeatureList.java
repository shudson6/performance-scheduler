package performancescheduler.gui;

import javax.swing.JList;

import performancescheduler.data.Feature;

@SuppressWarnings("serial")
public class FeatureList extends JList<Feature> {
    public FeatureList(FeatureListModel model) {
        super(model);
        setCellRenderer(new FeaturePanelCellRenderer());
        setTransferHandler(new FeatureTransferHandler());
        setDragEnabled(true);
    }
}
