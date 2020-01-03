package performancescheduler.gui.dialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import javax.swing.SwingUtilities;
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
    	Insets insets = new Insets(2, 2, 2, 2);
        pane = new JPanel(new GridBagLayout());
        
        titleLabel = new JLabel("Title");
        GridBagConstraints titleLabelConst = new GridBagConstraints();
        titleLabelConst.gridx = 0;
        titleLabelConst.gridy = 0;
        titleLabelConst.anchor = GridBagConstraints.LINE_END;
        titleLabelConst.insets = insets;
        pane.add(titleLabel, titleLabelConst);
        titleField = new JTextField(32);
        GridBagConstraints titleFieldConst = new GridBagConstraints();
        titleFieldConst.gridx = 1;
        titleFieldConst.gridy = 0;
        titleFieldConst.gridwidth = 6;
        titleFieldConst.fill = GridBagConstraints.HORIZONTAL;
        titleFieldConst.insets = insets;
        pane.add(titleField, titleFieldConst);
        
        is3dCheckBox = new JCheckBox("3D");
        is3dCheckBox.setHorizontalTextPosition(JCheckBox.LEFT);
        GridBagConstraints is3dConst = new GridBagConstraints();
        is3dConst.gridx = 1;
        is3dConst.gridy = 1;
        is3dConst.anchor = GridBagConstraints.LINE_START;
        pane.add(is3dCheckBox, is3dConst);
        
        ratingLabel = new JLabel("Rating");
        GridBagConstraints ratingLabelConst = new GridBagConstraints();
        ratingLabelConst.gridx = 0;
        ratingLabelConst.gridy = 2;
        ratingLabelConst.gridwidth = 2;
        ratingLabelConst.anchor = GridBagConstraints.LINE_END;
        ratingLabelConst.insets = insets;
        pane.add(ratingLabel, ratingLabelConst);
        ratingComboBox = new JComboBox<>(new DefaultComboBoxModel<>(Rating.values()));
        GridBagConstraints ratingComboConst = new GridBagConstraints();
        ratingComboConst.gridx = 2;
        ratingComboConst.gridy = 2;
        ratingComboConst.gridwidth = 2;
        ratingComboConst.anchor = GridBagConstraints.LINE_START;
        ratingComboConst.insets = insets;
        pane.add(ratingComboBox, ratingComboConst);
        
        runtimeLabel = new JLabel("Runtime");
        GridBagConstraints runtimeLabelConst = new GridBagConstraints();
        runtimeLabelConst.gridx = 0;
        runtimeLabelConst.gridy = 3;
        runtimeLabelConst.gridwidth = 2;
        runtimeLabelConst.anchor = GridBagConstraints.LINE_END;
        runtimeLabelConst.insets = insets;
        pane.add(runtimeLabel, runtimeLabelConst);
        runtimeSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 720, 1));
        GridBagConstraints runtimeSpinnerConst = new GridBagConstraints();
        runtimeSpinnerConst.gridx = 2;
        runtimeSpinnerConst.gridy = 3;
        runtimeSpinnerConst.gridwidth = 2;
        runtimeSpinnerConst.anchor = GridBagConstraints.LINE_START;
        runtimeSpinnerConst.insets = insets;
        pane.add(runtimeSpinner, runtimeSpinnerConst);
        
        amenitiesPanel = initAmenititesPanel();
        GridBagConstraints amenitiesConst = new GridBagConstraints();
        amenitiesConst.gridx = 4;
        amenitiesConst.gridy = 1;
        amenitiesConst.gridwidth = 3;
        amenitiesConst.gridheight = 3;
        amenitiesConst.anchor = GridBagConstraints.LINE_END;
        pane.add(amenitiesPanel, amenitiesConst);
        
        confirmButton = new JButton("Confirm");
        GridBagConstraints confirmConst = new GridBagConstraints();
        confirmConst.gridx = 1;
        confirmConst.gridy = 4;
        confirmConst.gridwidth = 2;
        confirmConst.insets = insets;
        pane.add(confirmButton, confirmConst);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        GridBagConstraints cancelConst = new GridBagConstraints();
        cancelConst.gridx = 3;
        cancelConst.gridy = 4;
        cancelConst.gridwidth = 2;
        cancelConst.insets = insets;
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
    	SwingUtilities.invokeLater(() -> {
    		new FeatureDialog(null).setVisible(true);
    		System.exit(0);
    	});
    }
}
