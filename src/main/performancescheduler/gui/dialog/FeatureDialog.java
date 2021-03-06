package performancescheduler.gui.dialog;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;


@SuppressWarnings("serial")
public class FeatureDialog extends JDialog {
	
	private boolean confirmed = false;
	private FeatureFactory featureFactory;

    private JPanel pane;
    private JLabel titleLabel;
    private JTextField titleField;
    private JLabel ratingLabel;
    private JComboBox<Rating> ratingComboBox;
    private ComboBoxModel<Rating> ratingComboBoxModel;
    private JLabel runtimeLabel;
    private JSpinner runtimeSpinner;
    private JLabel is3dLabel;
    private JCheckBox is3dCheckBox;
    private JPanel amenitiesPanel;
    private JCheckBox ccCheckBox;
    private JCheckBox ocCheckBox;
    private JCheckBox daCheckBox;
    private JButton confirmButton;
    private JButton cancelButton;
    
    // for testability, any dialog spawned by this one needs to be reachable for automatic close
    private JDialog childDialog;
    
    public FeatureDialog(Frame parent, FeatureFactory ftrFact) {
        super(parent, true);
        Objects.requireNonNull(ftrFact);
        
        featureFactory = ftrFact;
        init();
    }
    
    public boolean showNewFeatureDialog() {
    	confirmButton.setText("Create");
    	setLocationRelativeTo(getParent());
    	pack();
    	setVisible(true);
    	return confirmed;
    }
    
    public Feature getCreatedFeature() {
    	if (confirmed) {
    		return featureFactory.createFeature(titleField.getText(), 
    				ratingComboBox.getItemAt(ratingComboBox.getSelectedIndex()),
    				(Integer) runtimeSpinner.getValue(),
    				is3dCheckBox.isSelected(),
    				ccCheckBox.isSelected(),
    				ocCheckBox.isSelected(),
    				daCheckBox.isSelected());
    	} else {
    		return null;
    	}
    }
    
    protected boolean validateInputs() {
    	// honestly we just need a non-empty text field. the other values are constrained by the nature of their
    	// input methods
    	return !titleField.getText().isEmpty();
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
        titleField = initTitleField();
        GridBagConstraints titleFieldConst = new GridBagConstraints();
        titleFieldConst.gridx = 1;
        titleFieldConst.gridy = 0;
        titleFieldConst.gridwidth = 6;
        titleFieldConst.fill = GridBagConstraints.HORIZONTAL;
        titleFieldConst.insets = insets;
        pane.add(titleField, titleFieldConst);
        
        is3dLabel = new JLabel("3D");
        GridBagConstraints is3dLC = new GridBagConstraints();
        is3dLC.gridx = 0;
        is3dLC.gridy = 1;
        is3dLC.anchor = GridBagConstraints.LINE_END;
        is3dLC.insets = insets;
        pane.add(is3dLabel, is3dLC);
        is3dCheckBox = new JCheckBox();
        GridBagConstraints is3dConst = new GridBagConstraints();
        is3dConst.gridx = 1;
        is3dConst.gridy = 1;
        is3dConst.anchor = GridBagConstraints.LINE_START;
        is3dConst.insets = insets;
        pane.add(is3dCheckBox, is3dConst);
        
        ratingLabel = new JLabel("Rating");
        GridBagConstraints ratingLabelConst = new GridBagConstraints();
        ratingLabelConst.gridx = 2;
        ratingLabelConst.gridy = 1;
        ratingLabelConst.gridwidth = 1;
        ratingLabelConst.anchor = GridBagConstraints.LINE_END;
        ratingLabelConst.insets = insets;
        pane.add(ratingLabel, ratingLabelConst);
        ratingComboBoxModel = new DefaultComboBoxModel<>(Rating.values());
        ratingComboBox = new JComboBox<>(ratingComboBoxModel);
        GridBagConstraints ratingComboConst = new GridBagConstraints();
        ratingComboConst.gridx = 3;
        ratingComboConst.gridy = 1;
        ratingComboConst.gridwidth = 1;
        ratingComboConst.anchor = GridBagConstraints.LINE_START;
        ratingComboConst.insets = insets;
        pane.add(ratingComboBox, ratingComboConst);
        
        runtimeLabel = new JLabel("Runtime");
        GridBagConstraints runtimeLabelConst = new GridBagConstraints();
        runtimeLabelConst.gridx = 1;
        runtimeLabelConst.gridy = 2;
        runtimeLabelConst.gridwidth = 2;
        runtimeLabelConst.anchor = GridBagConstraints.LINE_END;
        runtimeLabelConst.insets = insets;
        pane.add(runtimeLabel, runtimeLabelConst);
        runtimeSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 720, 1));
        GridBagConstraints runtimeSpinnerConst = new GridBagConstraints();
        runtimeSpinnerConst.gridx = 3;
        runtimeSpinnerConst.gridy = 2;
        runtimeSpinnerConst.gridwidth = 1;
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
        confirmButton.addActionListener(e -> confirmButtonClicked());
        GridBagConstraints confirmConst = new GridBagConstraints();
        confirmConst.gridx = 0;
        confirmConst.gridy = 3;
        confirmConst.gridwidth = 2;
        confirmConst.insets = insets;
        pane.add(confirmButton, confirmConst);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelButtonClicked());
        GridBagConstraints cancelConst = new GridBagConstraints();
        cancelConst.gridx = 2;
        cancelConst.gridy = 3;
        cancelConst.gridwidth = 2;
        cancelConst.insets = insets;
        pane.add(cancelButton, cancelConst);
        
        setContentPane(pane);
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
    
    private JTextField initTitleField() {
        JTextField text = new JTextField(32);
        text.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
                if (getLength() + str.length() < 32) {
                    super.insertString(offset, str, attr);
                }
            }
        });
        text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // check the 3D box if the title ends in 3D
                try {
                    if (e.getDocument().getText(e.getDocument().getLength() - 2, 2).equalsIgnoreCase("3D")) {
                        is3dCheckBox.setSelected(true);
                    }
                } catch (BadLocationException e1) {
                    // this will only occur if the document is too short to contain what we are looking for
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                // not interested
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                // not interested
            }
        });
        return text;
    }
    
    private void confirmButtonClicked() {
        if (!validateInputs()) {
            childDialog = new JOptionPane("Please give the feature a title.", JOptionPane.ERROR_MESSAGE)
                    .createDialog(this, "No title");
            childDialog.setVisible(true);
            return;
        }
    	confirmed = true;
    	dispose();
    }
    
    private void cancelButtonClicked() {
    	confirmed = false;
    	dispose();
    }
}
