package performancescheduler.gui.dialog;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.TestData;

public class FeatureDialogTest {
    private static FeatureDialog dialog;
    private static Robot robot; 
    
    @BeforeClass
    public static void beforeClass() throws AWTException {
        dialog = new FeatureDialog(null, TestData.featureFactory);
        robot = new Robot();
    }

    @Test
    public void verifyInitCancelAndNoFeature() {
        FeatureDialog dialog = new FeatureDialog(null, TestData.featureFactory);
        SwingUtilities.invokeLater(() -> dialog.showNewFeatureDialog());
        assertEquals(null, dialog.getCreatedFeature());
        while (!dialog.isVisible());
        dialog.clickCancel();
        while (dialog.isVisible());
    }

    @Test
    public void testTitleFieldSets3D() throws BadLocationException {
        assertFalse(dialog.isVisible());
        SwingUtilities.invokeLater(() -> dialog.showNewFeatureDialog());
        while (!dialog.isVisible()) {}
        dialog.getTitleField().getDocument().insertString(0, "Hello 3D", null);
        dialog.clickConfirm();
        assertTrue(dialog.getCreatedFeature().is3d());
        assertFalse(dialog.isVisible());
    }
}
