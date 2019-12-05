package performancescheduler.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import performancescheduler.entity.DataEvent;
import performancescheduler.entity.DataListener;
import performancescheduler.entity.DataManager;
import performancescheduler.util.UUIDGenerator;

public class DataManagerTest {
    DataManager<String> mgr;
    
    @Before
    public void setUp() {
        mgr = new DataManager<>();
    }
    
    @Test
    public void testAddItemVerifyEventVerifyStructure() {
        // listener will verify the expected event gets fired
        mgr.addDataListener(event -> {
            assertEquals(mgr, event.getSource());
            assertEquals("foo", event.getPreviousState()[0]);
            assertEquals("foo", event.getUpdatedState()[0]);
            assertEquals(DataEvent.ADD_SINGLE, event.getOperation());
        });
        // shouldn't add null
        assertFalse(mgr.addItem(null));
        // should succeed
        assertTrue(mgr.addItem("foo"));
        // a few tests to verify the internal structure
        assertTrue(mgr.contains("foo"));
        assertEquals("foo", mgr.getData(mgr.getUUID("foo")));
        assertEquals(1, mgr.getAll().size());
        assertTrue(mgr.getAll().contains("foo"));
        assertFalse(mgr.contains("bar"));
        assertFalse(mgr.getAll().contains("bar"));
        assertEquals("foo", mgr.getOriginal(mgr.getUUID("foo")));
        assertFalse(mgr.wasDeleted(mgr.getUUID("foo")));
        // should fail to add item that matches a present state
        assertFalse(mgr.addItem("foo"));
    }
    
    @Test
    public void addMultipleVerifyStructureVerifyEventFired() {
        // listener will verify the expected event is fired
        mgr.addDataListener(event -> {
            assertEquals(mgr, event.getSource());
            Map<Object, Object> map = event.getUpdateMap();
            assertTrue(map.containsKey("foo"));
            assertTrue(map.containsKey("bar"));
        });
        ArrayList<String> stuff = new ArrayList<>();
        stuff.add("foo");
        stuff.add("bar");
        // both should be added
        assertTrue(mgr.addItems(stuff));
        assertTrue(mgr.contains("foo"));
        assertTrue(mgr.contains("bar"));
        assertNotEquals(mgr.getUUID("foo"), mgr.getUUID("bar"));
        // neither should be added twice
        assertFalse(mgr.addItems(stuff));
        assertEquals(2, mgr.getAll().size());
    }
    
    @Test
    public void updateItemShouldKeepSameUuid() {
        mgr.addItem("foo");
        UUID uuid = mgr.getUUID("foo");
        // update should go through
        assertTrue(mgr.updateItem("foo", "bar"));
        // now bar should have the same uuid
        assertEquals(uuid, mgr.getUUID("bar"));
    }
    
    @Test
    public void multipleUpdateItemsShouldKeepSameUuidMapping() {
        mgr.addItem("foo");
        mgr.addItem("bar");
        UUID fooId = mgr.getUUID("foo");
        UUID barId = mgr.getUUID("bar");
        assertNotEquals(fooId, barId);
        // create a map for updating the items
        Map<String, String> map = new TreeMap<>();
        map.put("foo", "poo");
        map.put("bar", "par");
        // add a data listener to verify the data event
        mgr.addDataListener(event -> {
            assertEquals(mgr, event.getSource());
            Map<Object, Object> upd8 = event.getUpdateMap();
            assertEquals("poo", upd8.get("foo"));
            assertEquals("par", upd8.get("bar"));
            assertEquals(DataEvent.UPDATE_MULTIPLE, event.getOperation());
        });
        // update
        assertTrue(mgr.updateItems(map));
        // uuids should have "swapped" since i used the same names (confusing?)
        assertEquals(fooId, mgr.getUUID("poo"));
        assertEquals(barId, mgr.getUUID("par"));
        assertNotEquals(mgr.getUUID("poo"), mgr.getUUID("par"));
    }
    
    @Test
    public void cantUpdateItemThatIsntThere() {
        assertFalse(mgr.updateItem("foo", "bar"));
    }
    
    @Test
    public void cantUpdateMultipleWithNullMap() {
        assertFalse(mgr.updateItems(null));
    }
    
    @Test
    public void cantUpdateMultipleWithEmptyMap() {
        assertFalse(mgr.updateItems(new TreeMap<String, String>()));
    }
    
    @Test
    public void cantUpdateStatesThatArentThere() {
        Map<String, String> stuff = new TreeMap<>();
        stuff.put("foo", "bar");
        assertFalse(mgr.updateItems(stuff));
    }
    
    @Test
    public void UuidOfNullIsNull() {
        assertEquals(null, mgr.getUUID(null));
    }
    
    @Test
    public void UuidOfNonexistentObjectIsNull() {
        assertEquals(null, mgr.getUUID("foo"));
    }
    
    @Test
    public void cantGetOriginalOfNonexistentUuid() {
        assertEquals(null, mgr.getOriginal(new UUIDGenerator().generateUUID()));
    }
    
    @Test
    public void cantGetOriginalOfNullUuid() {
        assertEquals(null, mgr.getOriginal(null));
    }
    
    @Test
    public void cantGetDataForNullUuid() {
        assertEquals(null, mgr.getData(null));
    }
    
    @Test
    public void cantGetDataForNonexistentUuid() {
        assertEquals(null, mgr.getData(new UUIDGenerator().generateUUID()));
    }
    
    @Test
    public void cantAddSameDataListenerTwice() {
        DataListener dl = event -> {};
        mgr.addDataListener(dl);
        // not that there's any indication that the addition is refused...
        mgr.addDataListener(dl);
        mgr.removeDataListener(dl);
    }
    
    @Test
    public void cantAddNullDataListener() {
        mgr.addDataListener(null);
    }
    
    @Test
    public void cantRemoveNullDataListener() {
        mgr.removeDataListener(null);
    }
    
    @Test
    public void couldntHaveDeletedNullItem() {
        assertFalse(mgr.wasDeleted(null));
    }
    
    @Test
    public void cantDeleteItIfItIsntThere() {
        assertFalse(mgr.wasDeleted(new UUIDGenerator().generateUUID()));
    }
    
    @Test
    public void wasDeletedButItWasnt() {
        mgr.addItem("foo");
        assertFalse(mgr.wasDeleted(mgr.getUUID("foo")));
    }
    
    @Test
    public void wasDeleted() {
        mgr.addItem("foo");
        UUID uuid = mgr.getUUID("foo");
        assertTrue(mgr.updateItem("foo", null));
        assertTrue(mgr.wasDeleted(uuid));
    }
}
