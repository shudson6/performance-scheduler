package performancescheduler.gui;

import static org.junit.Assert.*;

import java.awt.Point;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.TestData;
import performancescheduler.core.PerformanceDataModel;
import performancescheduler.core.PerformanceManager;

public class PerformanceGraphTest {
	static PerformanceGraph graph;
	static PerformanceManager manager;
	static PerformanceGraphModel model;
	static LocalDateTime start;
	
	@BeforeClass
	public static void setUpBefore() {
		manager = new PerformanceManager(new PerformanceDataModel());
		start = LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 0));
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
	public void getPerformanceAtPoint() {
		Point p = new Point(graph.convertTimeToCoordinateX(TestData.pfmFoo1.getDateTime()),
				graph.convertAudNumToCoordinateY(TestData.pfmFoo1.getAuditorium()));
		model.add(TestData.pfmFoo1);
		// should get null b/c the cell is smaller to fit w/in the gridlines
		assertEquals(null, graph.getPerformanceAt(p));
		p.x += 5;
		p.y += graph.getAuditoriumHeight() / 2;
		assertEquals(TestData.pfmFoo1, graph.getPerformanceAt(p));
	}
}
