package performancescheduler.core;

import static org.junit.Assert.*;

import javax.swing.undo.UndoManager;

import org.junit.Test;

import performancescheduler.TestData;

public class UndoTest {

    @Test
    public void testUndoRedoAddPerformance() {
        PerformanceManager mgr = new PerformanceManager(new PerformanceDataModel());
        mgr.setUndoManager(new UndoManager());
        mgr.addPerformance(TestData.pfmFooBravo);
        assertTrue(mgr.model.contains(TestData.pfmFooBravo));
        mgr.getUndoManager().undo();
        assertFalse(mgr.model.contains(TestData.pfmFooBravo));
        mgr.getUndoManager().redo();
        assertTrue(mgr.model.contains(TestData.pfmFooBravo));
    }

}
