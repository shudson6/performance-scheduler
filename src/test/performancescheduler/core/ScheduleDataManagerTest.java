package performancescheduler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;

public class ScheduleDataManagerTest extends ScheduleDataManager<String> {
    boolean fired;
    
    @Before
    public void setUp() { 
        fired = false;
    }
    
    @Test
    public void testEventFiresOnAdd() {
        addScheduleDataListener(event -> {
            assertTrue(event.getAdded().contains("Fired!"));
            fired = true;
        });
        add("Fired!");
        if (!fired) {
            fail("Add event didn't fire!");
        }
    }
    
    @Test
    public void testEventFiresOnRemove() {
        add("Foo");
        addScheduleDataListener(e -> {
            assertTrue(e.getRemoved().contains("Foo"));
            fired = true;
        });
        remove("Foo");
        if (!fired) {
            fail("Remove event didn't fire!");
        }
    }
    
    @Test
    public void testEventFiresOnUpdate() {
        add("Foo");
        addScheduleDataListener(e -> {
            assertTrue(e.getRemoved().contains("Foo"));
            assertTrue(e.getAdded().contains("Bar"));
            fired = true;
        });
        update("Foo", "Bar");
        if (!fired) {
            fail("Update event didn't fire!");
        }
    }
    
    @Test
    public void testNoEventsFireWhenDisabled() {
        setEventsEnabled(false);
        assertFalse(areEventsEnabled());
        addScheduleDataListener(e -> fail("Should not have fired an event."));
        assertTrue(add("Hi"));
        assertTrue(update("Hi", "Bye"));
        assertTrue(remove("Bye"));
        fireEvent(eventFactory.newAddEvent(Arrays.asList("Fake")));
    }
    
    @Test
    public void dontAddSameListenerTwice() {
        ScheduleDataListener<String> listener = new ScheduleDataListener<>() {
            @Override
            public void scheduleDataChanged(ScheduleEvent<String> event) {
                fail("No event expected.");
            }
        };
        addScheduleDataListener(listener);
        addScheduleDataListener(listener);
        removeScheduleDataListener(listener);
        assertTrue(areEventsEnabled());
        assertTrue(add("Listener list should be empty"));
    }
    
    @Test
    public void testAddRemoveUpdateFailures() {
        assertTrue(add("Foo"));
        // using a Set for data to ensure this second add fails
        assertFalse(add("Foo"));
        assertFalse(remove("Bar"));
        assertFalse(update("Foobar", "Bar"));
    }
    
    @Test
    public void testSize() {
        add("Foo");
        assertEquals(1, size());
        add("Bar");
        assertEquals(2, size());
    }
    
    public ScheduleDataManagerTest() {
        super();
        data = new TreeSet<>();
    }

    @Override
    public Collection<String> getData() {
        return data;
    }

    @Override
    public void setData(Collection<String> newData) {
        // not in this test
    }
}