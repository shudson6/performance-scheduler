package performancescheduler.gui;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import javax.swing.TransferHandler;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.TestData;
import performancescheduler.core.PerformanceDataModel;
import performancescheduler.core.PerformanceManager;
import performancescheduler.data.Performance;

public class PerformanceGraphTest {
	static PerformanceGraph graph;
	static PerformanceManager manager;
	static PerformanceGraphModel model;
	static LocalDateTime start;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBefore() {
		manager = new PerformanceManager(new PerformanceDataModel());
		start = LocalDateTime.of(TestData.ldtJulin.toLocalDate(), LocalTime.of(0, 0));
		model = new PerformanceGraphModel(start, null);
		graph = new PerformanceGraph(manager, model);
	}

	@Test
	public void convertXtoTime() {
		assertEquals(start, graph.convertCoordinateXtoTime(0));
		assertEquals(adjustedTime(300), graph.convertCoordinateXtoTime(300));
		assertEquals(adjustedTime(-300), graph.convertCoordinateXtoTime(-300));
	}

	private LocalDateTime adjustedTime(int xCoord) {
		return start.plusMinutes(xCoord / graph.getPixelsPerMinute());
	}
	
	@Test
	public void convertAudNumToY() {
		assertEquals(0, graph.convertAudNumToCoordinateY(1));
		assertEquals(graph.getAuditoriumHeight() * 3, graph.convertAudNumToCoordinateY(4));
		assertEquals(graph.getAuditoriumHeight() * -1, graph.convertAudNumToCoordinateY(0));
	}
	
	@Test
	public void convertYtoAudNum() {
		assertEquals(1, graph.convertCoordinateYtoAudNum(0));
		assertEquals(1, graph.convertCoordinateYtoAudNum(graph.getAuditoriumHeight() / 2));
		assertEquals(1, graph.convertCoordinateYtoAudNum(graph.getAuditoriumHeight() - 1));
		assertEquals(3, graph.convertCoordinateYtoAudNum(graph.getAuditoriumHeight() * 7 / 3));
	}
	
	@Test
	public void convertTimeToX() {
		assertEquals(0, graph.convertTimeToCoordinateX(graph.getModel().getRangeStart()));
		assertEquals(300 * graph.getPixelsPerMinute(), graph.convertTimeToCoordinateX(start.plusMinutes(300)));
		assertEquals(-50 * graph.getPixelsPerMinute(), graph.convertTimeToCoordinateX(start.plusMinutes(-50)));
	}
	
	@Test
	public void verifyDefaultCellRenderer() {
		assertTrue(graph.getPerformanceCellRenderer() instanceof PerformanceGraphCellRenderer);
	}
	
	@Test
	public void testTransferStartPoint() {
	    graph.getTransferHandler().setXferStartPoint(new Point(37, 43));
	    assertEquals(new Point(37, 43), 
	            ((PerformanceGraph.TransferHandler.PerformanceTransfer)
	                    graph.getTransferHandler().createTransferable(null))
	            .getStartPoint());
	}
	
	@Test
	public void verifyDataFlavors() {
	    Transferable t = graph.getTransferHandler().createTransferable(graph);
	    assertArrayEquals(new DataFlavor[] {App.performanceFlavor}, t.getTransferDataFlavors());
	    assertTrue(t.isDataFlavorSupported(App.performanceFlavor));
	    assertFalse(t.isDataFlavorSupported(App.featureFlavor));
	}
	
	@Test
	public void shouldGetUnsupportedFlavorException() throws UnsupportedFlavorException, IOException {
	    exception.expect(UnsupportedFlavorException.class);
	    graph.getTransferHandler().createTransferable(graph).getTransferData(App.featureFlavor);
	}
	
	@SuppressWarnings("unchecked")
    @Test
	public void testPerformanceTransferData() throws UnsupportedFlavorException, IOException {
	    assertTrue(graph.getModel().add(TestData.pfmFoo1));
	    graph.getSelectionModel().setSelectionInterval(0, 0);
	    graph.getTransferHandler().setXferStartPoint(new Point(0, 0));
	    Collection<Performance> cp = (Collection<Performance>) graph.getTransferHandler().createTransferable(null)
	            .getTransferData(App.performanceFlavor);
	    assertEquals(1, cp.size());
	    assertTrue(cp.contains(TestData.pfmFoo1));
	    graph.getModel().remove(TestData.pfmFoo1);
	    assertEquals(0, graph.getModel().size());
	}
	
	@Test
	public void verifySourceActions() {
	    assertEquals(TransferHandler.COPY_OR_MOVE, graph.getTransferHandler().getSourceActions(graph));
	}
	
	@Test
	public void verifyUIClassID() {
	    assertEquals("performanceGraphUI", graph.getUIClassID());
	    assertTrue(graph.getUI() instanceof PerformanceGraphUI);
	}
}
