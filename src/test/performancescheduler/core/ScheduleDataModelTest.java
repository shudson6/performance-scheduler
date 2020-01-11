package performancescheduler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import performancescheduler.core.event.ScheduleDataListener;
import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.data.Performance;

public class ScheduleDataModelTest extends ScheduleDataModel<String> {
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
    public void testEventFiredOnMultiAdd() {
    	Collection<String> added = new ArrayList<>();
    	addScheduleDataListener(event -> {
    		added.addAll(event.getAdded());
    		fired = true;
    	});
    	add(Arrays.asList("Foo", "Bar", "Foo"));
    	assertTrue(fired);
    	assertTrue(added.containsAll(Arrays.asList("Foo", "Bar")));
    	assertTrue(data.containsAll(added));
    	assertEquals(2, added.size());
    	assertEquals(2, data.size());
    }
    
    @Test
    public void verifyEmptyMultiAdd() {
    	assertFalse(add(Arrays.asList()));
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
    public void testEventFiredOnMultiRemove() {
    	Collection<String> rm = new ArrayList<>();
    	data.add("Foo");
    	data.add("Bar");
    	addScheduleDataListener(e -> {
    		rm.addAll(e.getRemoved());
    		fired = true;
    	});
    	remove(Arrays.asList("Foo", "foo", "Bar"));
    	assertTrue(fired);
    	assertTrue(rm.containsAll(Arrays.asList("Foo", "Bar")));
    	assertTrue(data.isEmpty());
    	assertEquals(2, rm.size());
    }
    
    @Test
    public void verifyEmptyMultiRemove() {
    	assertFalse(remove(Arrays.asList()));
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
    public void testEventFiredOnMultiUpdate() {
    	data.add("Foo");
    	data.add("Bar");
    	addScheduleDataListener(e -> {
    		assertTrue(e.getAdded().containsAll(Arrays.asList("Bar", "Pi")));
    		assertTrue(e.getRemoved().containsAll(Arrays.asList("Foo", "Bar")));
    		fired = true;
    	});
    	Map<String, String> updates = new HashMap<>();
    	updates.put("Foo", "Bar");
    	updates.put("Bar", "Pi");
    	update(updates);
    	assertTrue(fired);
    	assertFalse(data.contains("Foo"));
    }
    
    @Test
    public void verifyNoUpdateWhenNoSuchElement() {
    	Map<String, String> map = new HashMap<>();
    	map.put("Foo", "Bar");
    	addScheduleDataListener(e -> fail("No event expected."));
    	assertFalse(update(map));
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
        ScheduleDataListener<String> listener = e -> fail("No event expected.");
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
    
    @Test
    public void shouldFireRemoveEventIfAfterCantBeAdded() {
        add("Foo");
        add("Bar");
        addScheduleDataListener(e -> {
            assertEquals(ScheduleEvent.REMOVE, e.getAction());
            assertTrue(e.getRemoved().contains("Bar"));
            fired = true;
        });
        assertTrue(update("Bar", "Foo"));
        assertFalse(contains("Bar"));
        assertTrue(contains("Foo"));
        if (!fired) {
            fail("Event did not fire.");
        }
    }
    
    @Test
    public void setDataNullWithEvents() {
    	data.add("foo");
    	addScheduleDataListener(e -> {
    		assertEquals(ScheduleEvent.REMOVE, e.getAction());
    		fired = true;
    	});
    	setData(null);
    	assertTrue(fired);
    	assertTrue(data.isEmpty());
    }
    
    @Test
    public void setDataEmptyOnEmptyWithEvents() {
    	addScheduleDataListener(e -> fail("No event expected."));
    	setData(new ArrayList<>());
    	assertTrue(data.isEmpty());
    }
    
    @Test
    public void setDataNonmtOnNonmtWithEvents() {
    	data.add("foo");
    	addScheduleDataListener(e -> {
    		if (!fired) {
    			assertEquals(ScheduleEvent.REMOVE, e.getAction());
    			fired = true;
    		} else {
    			assertEquals(ScheduleEvent.ADD, e.getAction());
    			data.add("did it!");
    		}
    	});
    	setData(Arrays.asList("bar"));
    	assertTrue(fired);
    	assertTrue(data.containsAll(Arrays.asList("bar", "did it!")));
    }
    
    @Test
    public void setDataNoEvents() {
    	setEventsEnabled(false);
    	addScheduleDataListener(e -> fail("No event expected."));
    	setData(Arrays.asList("foobar"));
    	assertTrue(data.contains("foobar"));
    }

    @Override
    public Collection<String> getData() {
        return data;
    }
    
    @Override
    protected Collection<String> initData() {
    	// testing with TreeSet makes it easy to get add() == false
        return new TreeSet<>();
    }
}
