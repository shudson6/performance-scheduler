package performancescheduler.core.event;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.core.event.ScheduleEvent;

public class ScheduleEventTest {
    List<String> abcList = Arrays.asList("a", "b", "c");
    List<String> xyzList = Arrays.asList("x", "y", "z");
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testAddEvent() {
        ScheduleEvent<String> event = new ScheduleEvent<>(abcList, null, ScheduleEvent.ADD);
        assertTrue(event.getAdded().containsAll(abcList));
        assertTrue(abcList.containsAll(event.getAdded()));
        assertTrue(event.getRemoved().isEmpty());
        assertEquals(ScheduleEvent.ADD, event.getAction());
    }
    
    @Test
    public void testRemoveEvent() {
        ScheduleEvent<String> event = new ScheduleEvent<>(null, abcList, ScheduleEvent.REMOVE);
        assertTrue(event.getRemoved().containsAll(abcList));
        assertTrue(abcList.containsAll(event.getRemoved()));
        assertTrue(event.getAdded().isEmpty());
        assertEquals(ScheduleEvent.REMOVE, event.getAction());
    }

    @Test
    public void testUpdateEvent() {
        ScheduleEvent<String> event = new ScheduleEvent<>(abcList, xyzList, ScheduleEvent.UPDATE);
        assertTrue(event.getRemoved().containsAll(xyzList));
        assertTrue(xyzList.containsAll(event.getRemoved()));
        assertTrue(event.getAdded().containsAll(abcList));
        assertTrue(abcList.containsAll(event.getAdded()));
        assertEquals(ScheduleEvent.UPDATE, event.getAction());
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionForInvalidAction() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Action");
        new ScheduleEvent<String>(null, null, -50);
    }
}
