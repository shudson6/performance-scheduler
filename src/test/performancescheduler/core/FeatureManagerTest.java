package performancescheduler.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.data.Feature;
import performancescheduler.data.Rating;

public class FeatureManagerTest {
    private FeatureManager mgr = new FeatureManager();
    private Feature ftr1 = mgr.getFeatureFactory().createFeature("Foo", Rating.PG, 90, false, false, false, false);
    private Feature ftr2 = mgr.getFeatureFactory().createFeature("Bar", Rating.R, 100, false, true, false, false);
    
    private boolean fired = false;
    
    @Test
    public void eventsOnEmptyOld() {
        mgr.addScheduleDataListener(e -> {
            assertEquals(ScheduleEvent.ADD, e.getAction());
            fired = true;
        });
        mgr.setData(Arrays.asList(ftr1));
        assertEquals(1, mgr.size());
        assertTrue(mgr.getData().contains(ftr1));
        assertTrue(fired);
    }
    
    @Test
    public void eventsOffEmptyOld() {
        mgr.addScheduleDataListener(e -> fail("No event expected."));
        mgr.setEventsEnabled(false);
        mgr.setData(Arrays.asList(ftr1));
        assertEquals(1, mgr.size());
        assertTrue(mgr.contains(ftr1));
    }
    
    @Test
    public void eventsOnNonemptyOldEmptyNew() {
        mgr.add(ftr1);
        mgr.addScheduleDataListener(e -> {
            assertEquals(ScheduleEvent.REMOVE, e.getAction());
            fired = true;
        });
        mgr.setData(new ArrayList<Feature>());
        assertEquals(0, mgr.size());
    }
    
    @Test
    public void eventsOnEmptyOldNullNewNoEvent() {
        mgr.addScheduleDataListener(e -> fail("No event expected."));
        mgr.setData(null);
        assertEquals(0, mgr.size());
    }
}
