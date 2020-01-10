package performancescheduler.gui;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

import performancescheduler.TestData;
import performancescheduler.core.PerformanceDataModel;
import performancescheduler.core.PerformanceManager;

public class PerformanceGraphCellRendererTest {

    @Test
    public void testBounds() {
        PerformanceManager manager = new PerformanceManager(new PerformanceDataModel());
        PerformanceGraph graph = new PerformanceGraph(manager, 
                new PerformanceGraphModel(LocalDateTime.of(TestData.ldtJulin.toLocalDate(), LocalTime.of(0, 0)), 
                        null));
        assertTrue(manager.getModel().add(TestData.pfmFoo1));
        Rectangle r = graph.getPerformanceCellRenderer().getCellRendererComponent(graph, TestData.pfmFoo1, false, false)
                .getBounds();
        assertEquals(graph.convertTimeToCoordinateX(TestData.pfmFoo1.getDateTime()), r.x);
        assertEquals(TestData.pfmFoo1.length() * graph.getPixelsPerMinute(), r.width);
    }

}
