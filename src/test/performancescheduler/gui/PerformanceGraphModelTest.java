package performancescheduler.gui;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.TestData;
import performancescheduler.core.event.ScheduleEvent;
import performancescheduler.core.event.ScheduleEventFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.gui.event.GraphDataEvent;
import performancescheduler.gui.event.GraphDataListener;

public class PerformanceGraphModelTest {
    PerformanceGraphModel model = new PerformanceGraphModel(TestData.ldtTrinity, TestData.ldtJulin);
    PerformanceFactory pFactory = PerformanceFactory.newFactory();
    ScheduleEventFactory eventFactory = ScheduleEventFactory.newFactory();
    Performance p1 = pFactory.createPerformance(TestData.ftrBar, TestData.ldtBravo, 1);
    Performance p2 = pFactory.createPerformance(TestData.ftrFoo, TestData.ldtBravo, 1);
    boolean fired = false;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldGetIllegalStateExceptionForDateRange() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Date range");
        new PerformanceGraphModel(LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Test
    public void getDateRange() {
        assertEquals(TestData.ldtTrinity, model.getRangeStart());
        assertEquals(TestData.ldtJulin, model.getRangeEnd());
    }
    
    @Test
    public void whatHappensWhenBothDatesAreNull() {
        new PerformanceGraphModel(null, null);
    }

    @Test
    public void testAddAndFire() {
        assertFalse(fired);
        model.addEventListener(e -> {
            assertEquals(GraphDataEvent.ADD, e.getAction());
            fired = true;
        });
        assertTrue(model.add(p1));
        assertTrue(fired);
        assertEquals(1, model.size());
        assertTrue(model.contains(p1));
    }

    @Test
    public void addShouldBeRejected() {
        assertTrue(model.areEventsEnabled());
        model.addEventListener(e -> fail("No event expected."));
        assertFalse(model.add(TestData.pfmFoo1));
    }
    
    @Test
    public void testRemoveAndFire() {
        assertFalse(fired);
        model.add(p1);
        model.addEventListener(e -> {
            assertEquals(GraphDataEvent.REMOVE, e.getAction());
            fired = true;
        });
        assertTrue(model.remove(p1));
        assertTrue(fired);
        assertEquals(0, model.size());
    }
    
    @Test
    public void testIteration() {
        Collection<Performance> c = new ArrayList<>();
        model.add(p1);
        for (Performance p : model) {
            c.add(p);
        }
        assertEquals(1, c.size());
        assertTrue(c.contains(p1));
    }
    
    @Test
    public void addAddRemoveEventListener() {
        GraphDataListener listener = e -> fail("No event expected.");
        model.addEventListener(listener);
        model.addEventListener(listener);
        model.removeEventListener(listener);
        // make sure the listener isn't there
        assertTrue(model.areEventsEnabled());
        model.add(p1);
    }
    
    @Test
    public void coverShortCircuitedBranchInAdd() {
        PerformanceGraphModel pgm = treeSetTestModel();
        assertTrue(pgm.add(p1));
        assertFalse(pgm.add(p1));
    }
    
    @Test
    public void testScheduleDataAddEvent() {
        model = treeSetTestModel();
        model.addEventListener(e -> {
            assertEquals(GraphDataEvent.ADD, e.getAction());
            assertTrue(e.getAdded().contains(p1));
            fired = true;
        });
        model.scheduleDataChanged(eventFactory.newAddEvent(Arrays.asList(p1)));
        model.addEventListener(e -> fail("No event expected."));
        model.scheduleDataChanged(eventFactory.newAddEvent(Arrays.asList(p1)));
        assertTrue(fired);
    }
    
    @Test
    public void testScheduleDataRemoveEvent() {
        model = treeSetTestModel();
        assertTrue(model.add(p1));
        model.addEventListener(e -> {
            assertEquals(GraphDataEvent.REMOVE, e.getAction());
            assertTrue(e.getRemoved().contains(p1));
            fired = true;
        });
        model.scheduleDataChanged(eventFactory.newRemoveEvent(Arrays.asList(p1)));
        model.addEventListener(e -> fail("No event expected."));
        model.scheduleDataChanged(eventFactory.newRemoveEvent(Arrays.asList(p1)));
        assertTrue(fired);
    }
    
    @Test
    public void testScheduleDataReplaceEvent() {
        model = treeSetTestModel();
        GraphDataListener listener = e -> {
            assertEquals(GraphDataEvent.ADD, e.getAction());
            fired = true;
        };
        model.addEventListener(listener);
        model.scheduleDataChanged(eventFactory.newUpdateEvent(Arrays.asList(p1), Arrays.asList(p2)));
        assertTrue(fired);
        assertTrue(model.contains(p1));
        model.removeEventListener(listener);
        fired = false;
        listener = e -> {
            assertEquals(GraphDataEvent.REPLACE, e.getAction());
            fired = true;
        };
        model.addEventListener(listener);
        model.scheduleDataChanged(eventFactory.newUpdateEvent(Arrays.asList(p2), Arrays.asList(p1)));
        assertTrue(fired);
        assertTrue(model.contains(p2));
        assertFalse(model.contains(p1));
        model.removeEventListener(listener);
        fired = false;
        listener = e -> {
            assertEquals(GraphDataEvent.REMOVE, e.getAction());
            fired = true;
        };
        model.add(p1);
        model.addEventListener(listener);
        model.scheduleDataChanged(eventFactory.newUpdateEvent(Arrays.asList(p2), Arrays.asList(p1)));
        assertTrue(fired);
        assertTrue(model.contains(p2));
        assertFalse(model.contains(p1));
    }
    
    @Test
    public void coverScheduleDataReplaceEventEmpty() {
        model.addEventListener(e -> fail("No event expected."));
        model.scheduleDataChanged(eventFactory.newUpdateEvent(Arrays.asList(), Arrays.asList()));
    }
    
    @Test
    public void coverDefaultInScheduleDataChanged() {
        model.addEventListener(e -> fail("No event expected."));
        model.scheduleDataChanged(new ScheduleEvent<Performance>() {
            @Override
            public List<Performance> getRemoved() {
                return null;
            }
            @Override
            public List<Performance> getAdded() {
                return null;
            }
            @Override
            public int getAction() {
                return 0;
            }
        });
    }
    
    @Test
    public void fireEventDisabled() {
        model.setEventsEnabled(false);
        model.fireEvent(GraphDataEvent.newAddEvent(p1));
    }
    
    @Test
    public void coverRemainingBranchesInAccept() {
        assertFalse(model.add(pFactory.createPerformance(TestData.ftrBar, LocalDateTime.MIN, 1)));
    }

    private PerformanceGraphModel treeSetTestModel() {
        return new PerformanceGraphModel(TestData.ldtTrinity, TestData.ldtJulin) {
            @Override
            protected Collection<Performance> initData() {
                return new TreeSet<>();
            }
        };
    }
}
