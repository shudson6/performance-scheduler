package performancescheduler.gui.event;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.TestData;

public class GraphDataEventTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldThrowIllegalArgumentExceptionForEmptyCollection() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("empty");
        GraphDataEvent.newAddEvent(Arrays.asList());
    }

    @Test
    public void testAdd() {
        GraphDataEvent event = GraphDataEvent.newAddEvent(TestData.pfmFoo1);
        assertEquals(GraphDataEvent.ADD, event.getAction());
        assertEquals(1, event.getAdded().size());
        assertTrue(event.getAdded().contains(TestData.pfmFoo1));
        assertTrue(event.getRemoved().isEmpty());
    }

    @Test
    public void testRemove() {
        GraphDataEvent event = GraphDataEvent.newRemoveEvent(TestData.pfmFoo1);
        assertEquals(GraphDataEvent.REMOVE, event.getAction());
        assertEquals(1, event.getRemoved().size());
        assertTrue(event.getRemoved().contains(TestData.pfmFoo1));
        assertTrue(event.getAdded().isEmpty());  
    }
    
    @Test
    public void testReplace() {
        GraphDataEvent event = GraphDataEvent.newReplaceEvent(TestData.pfmFoo1, TestData.pfmBar2);
        assertEquals(GraphDataEvent.REPLACE, event.getAction());
        assertEquals(1, event.getRemoved().size());
        assertEquals(1, event.getAdded().size());
        assertTrue(event.getRemoved().contains(TestData.pfmFoo1));
        assertTrue(event.getAdded().contains(TestData.pfmBar2));
    }
}
