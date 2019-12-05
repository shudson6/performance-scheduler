package performancescheduler.entity;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.entity.DataEvent;
import performancescheduler.entity.DataManager;

public class DataEventTest {
    static DataManager<String> mgr;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        mgr = new DataManager<String>();
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createAddEventNoItems() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("empty"));
        DataEvent.createAddEvent(mgr);
    }

    @Test
    public void createAddEventNullItems() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("null"));
        DataEvent.createAddEvent(mgr, (Object[]) null);
    }
    
    @Test
    public void createWithNullSource() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("source"));
        DataEvent.createAddEvent(null, "foo");
    }
    
    @Test
    public void createUpdateEventNullOld() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("state"));
        DataEvent.createUpdateEvent(null, null, null);
    }
    
    @Test
    public void createUpdateEventNullUpdateMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("null"));
        DataEvent.createUpdateEvent(null, null);
    }
    
    @Test
    public void createUpdateEventEmptyUpdateMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("empty"));
        DataEvent.createUpdateEvent(null, new TreeMap<Object, Object>());
    }
    
    @Test
    public void singleAdd() {
        DataEvent de = DataEvent.createAddEvent(mgr, "foo");
        assertEquals(DataEvent.ADD_SINGLE, de.getOperation());
        assertEquals("foo", de.getPreviousState()[0]);
        assertEquals("foo", de.getUpdatedState()[0]);
        assertEquals(mgr, de.getSource());
    }
    
    @Test
    public void multiAdd() {
        DataEvent de = DataEvent.createAddEvent(mgr, "foo", "bar");
        assertEquals(DataEvent.ADD_MULTIPLE, de.getOperation());
        Map<Object, Object> map = de.getUpdateMap();
        assertEquals("foo", map.get("foo"));
        assertEquals("bar", map.get("bar"));
        assertEquals(2, map.size());
    }
    
    @Test
    public void singleUpdate() {
        DataEvent de = DataEvent.createUpdateEvent(mgr, "foo", "bar");
        assertEquals(DataEvent.UPDATE_SINGLE, de.getOperation());
        assertEquals("foo", de.getPreviousState()[0]);
        assertEquals("bar", de.getUpdatedState()[0]);
        assertEquals(mgr, de.getSource());
        assertEquals("bar", de.getUpdateMap().get("foo"));
    }
    
    @Test
    public void singleUpdateUsingMap() {
        Map<Object, Object> map = new TreeMap<>();
        map.put("foo", "bar");
        DataEvent de = DataEvent.createUpdateEvent(mgr, map);
        assertEquals(DataEvent.UPDATE_SINGLE, de.getOperation());
        assertEquals("foo", de.getPreviousState()[0]);
        assertEquals("bar", de.getUpdatedState()[0]);
        assertEquals(mgr, de.getSource());
        assertEquals("bar", de.getUpdateMap().get("foo"));
    }
    
    @Test
    public void multipleUpdateUsingMap() {
        Map<Object, Object> map = new TreeMap<>();
        map.put("foo", "bar");
        map.put("knob", "jockey");
        DataEvent de = DataEvent.createUpdateEvent(mgr, map);
        assertEquals(DataEvent.UPDATE_MULTIPLE, de.getOperation());
        assertEquals("foo", de.getPreviousState()[0]);
        assertEquals("bar", de.getUpdatedState()[0]);
        assertEquals("knob", de.getPreviousState()[1]);
        assertEquals("jockey", de.getUpdatedState()[1]);
        assertEquals(mgr, de.getSource());
        assertEquals("bar", de.getUpdateMap().get("foo"));
    }
}
