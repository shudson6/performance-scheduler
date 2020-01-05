package performancescheduler.core.event;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Test;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;

public class EventFactoryTest {
    Feature ftr1 = FeatureFactory.newFactory().createFeature("Ftr1", Rating.G, 90, false, false, false, false);
    Feature ftr2 = FeatureFactory.newFactory().createFeature("Ftr2", Rating.PG, 100, false, true, true, true);
    Auditorium aud = Auditorium.getInstance(1, null, false, 100);
    LocalDateTime ldt = LocalDateTime.now();
    Performance pfm1 = PerformanceFactory.newFactory().createPerformance(ftr1, ldt, aud.getNumber());
    Performance pfm2 = PerformanceFactory.newFactory().createPerformance(ftr2, ldt, aud.getNumber());
    ScheduleEventFactory eventFactory = ScheduleEventFactory.newFactory();

    @Test
    public void testNewAddFeatureEvent() {
        ScheduleEvent<Feature> ev = eventFactory.newAddEvent(Arrays.asList(ftr1, ftr2));
        assertEquals(2, ev.getAdded().size());
        assertTrue(ev.getAdded().contains(ftr1));
        assertTrue(ev.getAdded().contains(ftr2));
        assertEquals(ScheduleEvent.ADD, ev.getAction());
    }

    @Test
    public void testNewAddPerformanceEvent() {
        ScheduleEvent<Performance> ev = eventFactory.newAddEvent(Arrays.asList(pfm1, pfm2));
        assertEquals(2, ev.getAdded().size());
        assertTrue(ev.getRemoved().isEmpty());
        assertTrue(ev.getAdded().contains(pfm1));
        assertTrue(ev.getAdded().contains(pfm2));
        assertEquals(ScheduleEvent.ADD, ev.getAction());
    }
    
    @Test
    public void testNewRemoveFeatureEvent() {
        ScheduleEvent<Feature> ev = eventFactory.newRemoveEvent(Arrays.asList(ftr1));
        assertEquals(1, ev.getRemoved().size());
        assertTrue(ev.getRemoved().contains(ftr1));
        assertTrue(ev.getAdded().isEmpty());
        assertEquals(ScheduleEvent.REMOVE, ev.getAction());
    }
    
    @Test
    public void testNewRemovePerformanceEvent() {
        ScheduleEvent<Performance> ev = eventFactory.newRemoveEvent(Arrays.asList(pfm1));
        assertEquals(1, ev.getRemoved().size());
        assertTrue(ev.getRemoved().contains(pfm1));
        assertTrue(ev.getAdded().isEmpty());
        assertEquals(ScheduleEvent.REMOVE, ev.getAction());
    }
    
    @Test
    public void testNewUpdateFeatureEvent() {
        ScheduleEvent<Feature> ev = eventFactory.newUpdateEvent(Arrays.asList(ftr1), Arrays.asList(ftr2));
        assertEquals(1, ev.getRemoved().size());
        assertEquals(1, ev.getAdded().size());
        assertTrue(ev.getRemoved().contains(ftr2));
        assertTrue(ev.getAdded().contains(ftr1));
        assertEquals(ScheduleEvent.UPDATE, ev.getAction());
    }
    
    @Test
    public void testNewUpdatePerformanceEvent() {
        ScheduleEvent<Performance> ev = eventFactory.newUpdateEvent(Arrays.asList(pfm2), Arrays.asList(pfm1));
        assertEquals(1, ev.getAdded().size());
        assertTrue(ev.getAdded().contains(pfm2));
        assertEquals(1, ev.getRemoved().size());
        assertTrue(ev.getRemoved().contains(pfm1));
        assertEquals(ScheduleEvent.UPDATE, ev.getAction());
    }
}
