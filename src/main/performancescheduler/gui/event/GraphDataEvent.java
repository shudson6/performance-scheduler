package performancescheduler.gui.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import performancescheduler.data.Performance;

public class GraphDataEvent {
    public static final int ADD = 0x0001;
    public static final int REMOVE = 0x0002;
    public static final int REPLACE = 0x0003; 
    
    public static GraphDataEvent newAddEvent(Performance toAdd) {
        Objects.requireNonNull(toAdd);
        return new GraphDataEvent(Arrays.asList(toAdd), null, ADD);
    }
    
    public static GraphDataEvent newAddEvent(Collection<Performance> toAdd) {
        validateCollection(toAdd);
        return new GraphDataEvent(toAdd, null, ADD);
    }
    
    public static GraphDataEvent newRemoveEvent(Performance toRem) {
        Objects.requireNonNull(toRem);
        return new GraphDataEvent(null, Arrays.asList(toRem), REMOVE);
    }
    
    public static GraphDataEvent newRemoveEvent(Collection<Performance> toRem) {
        validateCollection(toRem);
        return new GraphDataEvent(null, toRem, REMOVE);
    }
    
    public static GraphDataEvent newReplaceEvent(Performance before, Performance after) {
        Objects.requireNonNull(before);
        Objects.requireNonNull(after);
        return new GraphDataEvent(Arrays.asList(after), Arrays.asList(before), REPLACE);
    }
    
    public static GraphDataEvent newReplaceEvent(Collection<Performance> before, Collection<Performance> after) {
        validateCollection(before);
        validateCollection(after);
        return new GraphDataEvent(after, before, REPLACE);
    }
    
    private static void validateCollection(Collection<?> toAdd) {
        Objects.requireNonNull(toAdd);
        if (toAdd.isEmpty()) {
            throw new IllegalArgumentException("GraphDataEvent: input collection must not be empty.");
        }
    }
    
    private final Collection<Performance> added = new ArrayList<>();
    private final Collection<Performance> removed = new ArrayList<>();
    private final int action;
    
    public Collection<Performance> getAdded() {
        return new ArrayList<>(added);
    }
    
    public int getAction() {
        return action;
    }
    
    public Collection<Performance> getRemoved() {
        return new ArrayList<>(removed);
    }
    
    private GraphDataEvent(Collection<Performance> toAdd, Collection<Performance> toRemove, int act) {
        if (toAdd != null) {
            added.addAll(toAdd);
        }
        if (toRemove != null) {
            removed.addAll(toRemove);
        }
        action = act;
    }
}
