package performancescheduler.gui.dialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import performancescheduler.data.Rating;


public class FeatureDialog extends JDialog {

    private JPanel pane;
    private JLabel titleLabel;
    private JTextField titleField;
    private JLabel ratingLabel;
    private JComboBox<Rating> ratingComboBox;
    private JLabel runtimeLabel;
    private JSpinner runtimeSpinner;
    private JCheckBox is3dCheckBox;
    private JPanel amenitiesPanel;
    private JCheckBox ccCheckBox;
    private JCheckBox ocCheckBox;
    private JCheckBox daCheckBox;
    private JButton confirmButton;
    private JButton cancelButton;
    
    public FeatureDialog(Frame parent) {
        super(parent, true);
        init();
    }
    
    private void init() {
        pane = new JPanel(new GridBagLayout());
        
        titleLabel = new JLabel("Title");
        GridBagConstraints titleLabelConst = new GridBagConstraints();
        titleLabelConst.gridx = 0;
        titleLabelConst.gridy = 0;
        titleLabelConst.anchor = GridBagConstraints.LINE_END;
        pane.add(titleLabel, titleLabelConst);
        titleField = new JTextField(32);
        GridBagConstraints titleFieldConst = new GridBagConstraints();
        titleFieldConst.gridx = 1;
        titleFieldConst.gridy = 0;
        titleFieldConst.gridwidth = 8;
        titleFieldConst.fill = GridBagConstraints.HORIZONTAL;
        pane.add(titleField, titleFieldConst);
        
        is3dCheckBox = new JCheckBox("3D");
        is3dCheckBox.setHorizontalTextPosition(JCheckBox.RIGHT);
        GridBagConstraints is3dConst = new GridBagConstraints();
        is3dConst.gridx = 1;
        is3dConst.gridy = 1;
        is3dConst.anchor = GridBagConstraints.LINE_START;
        pane.add(is3dCheckBox, is3dConst);
        
        ratingLabel = new JLabel("Rating");
        GridBagConstraints ratingLabelConst = new GridBagConstraints();
        ratingLabelConst.gridx = 0;
        ratingLabelConst.gridy = 2;
        ratingLabelConst.anchor = GridBagConstraints.LINE_END;
        pane.add(ratingLabel, ratingLabelConst);
        ratingComboBox = new JComboBox<>(new DefaultComboBoxModel<>(Rating.values()));
        GridBagConstraints ratingComboConst = new GridBagConstraints();
        ratingComboConst.gridx = 1;
        ratingComboConst.gridy = 2;
        pane.add(ratingComboBox, ratingComboConst);
        
        runtimeLabel = new JLabel("Runtime");
        GridBagConstraints runtimeLabelConst = new GridBagConstraints();
        runtimeLabelConst.gridx = 0;
        runtimeLabelConst.gridy = 3;
        runtimeLabelConst.gridwidth = 2;
        runtimeLabelConst.anchor = GridBagConstraints.LINE_END;
        pane.add(runtimeLabel, runtimeLabelConst);
        runtimeSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 720, 1));
        GridBagConstraints runtimeSpinnerConst = new GridBagConstraints();
        runtimeSpinnerConst.gridx = 0;
        runtimeSpinnerConst.gridy = 3;
        pane.add(runtimeSpinner, runtimeSpinnerConst);
        
        amenitiesPanel = initAmenititesPanel();
        GridBagConstraints amenitiesConst = new GridBagConstraints();
        amenitiesConst.gridx = 5;
        amenitiesConst.gridy = 1;
        amenitiesConst.gridwidth = 4;
        amenitiesConst.gridheight = 3;
        pane.add(amenitiesPanel, amenitiesConst);
        
        confirmButton = new JButton("Confirm");
        GridBagConstraints confirmConst = new GridBagConstraints();
        confirmConst.gridx = 5;
        confirmConst.gridy = 4;
        confirmConst.gridwidth = 2;
        pane.add(confirmButton, confirmConst);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        GridBagConstraints cancelConst = new GridBagConstraints();
        cancelConst.gridx = 7;
        cancelConst.gridy = 4;
        cancelConst.gridwidth = 2;
        pane.add(cancelButton, cancelConst);
        
        setContentPane(pane);
        pack();
    }
    
    private JPanel initAmenititesPanel() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setAlignmentX(LEFT_ALIGNMENT);
        pane.setBorder(new TitledBorder(new EtchedBorder(), "Accessibility"));
        
        ccCheckBox = new JCheckBox("Closed Captions (CC)");
        ccCheckBox.setHorizontalTextPosition(JCheckBox.RIGHT);
        pane.add(ccCheckBox);
        ocCheckBox = new JCheckBox("Open Captions (OC)");
        ocCheckBox.setHorizontalTextPosition(JCheckBox.RIGHT);
        pane.add(ocCheckBox);
        daCheckBox = new JCheckBox("Descriptive Audio (DA)");
        daCheckBox.setHorizontalTextPosition(JCheckBox.RIGHT);
        pane.add(daCheckBox);
        
        return pane;
    }
    
    public static void main(String[] args) {
        new FeatureDialog(null).setVisible(true);
    }
}
