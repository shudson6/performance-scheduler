package performancescheduler.entity;

import java.util.Map;
import java.util.TreeMap;

public class DataEvent {
    public static final int ADD_SINGLE = 0;
    public static final int ADD_MULTIPLE = 1;
    public static final int UPDATE_SINGLE = 2;
    public static final int UPDATE_MULTIPLE = 3;
    
    public static DataEvent createAddEvent(DataManager<?> src, Object... items) {
        if (items == null) {
            throw new IllegalArgumentException("DataEvent.createAddEvent: items must be non-null.");
        } else if (items.length == 0) {
            throw new IllegalArgumentException("DataEvent.createAddEvent: items must be non-empty.");
        }
        int _op = (items.length == 1) ? ADD_SINGLE : ADD_MULTIPLE;
        TreeMap<Object, Object> map = new TreeMap<>();
        for (Object o : items) {
            map.put(o, o);
        }
        return new DataEvent(src, _op, map);
    }
    
    public static DataEvent createUpdateEvent(DataManager<?> src, Object old, Object cur) {
        if (old == null) {
            throw new IllegalArgumentException("DataManager.createUpdateEvent: old state must be non-null");
        }
        TreeMap<Object, Object> map = new TreeMap<>();
        map.put(old, cur);
        return new DataEvent(src, UPDATE_SINGLE, map);
    }
    
    public static DataEvent createUpdateEvent(DataManager<?> src, Map<Object, Object> updates) {
        if (updates == null) {
            throw new IllegalArgumentException("DataManager.createUpdateEvent: updates map must be non-null");
        } else if (updates.isEmpty()) {
            throw new IllegalArgumentException("DataManager.createUpdateEvent: updates map must be non-empty");
        }
        int _op = (updates.size() == 1) ? UPDATE_SINGLE : UPDATE_MULTIPLE;
        return new DataEvent(src, _op, updates);
    }
    
    private final DataManager<?> src;
    private final Map<Object, Object> updates;
    private final int op;
    
    public int getOperation() {
        return op;
    }
    
    public Object[] getPreviousState() {
        return updates.keySet().toArray();
    }
    
    public DataManager<?> getSource() {
        return src;
    }
    
    public Object[] getUpdatedState() {
        return updates.values().toArray();
    }
    
    public Map<Object, Object> getUpdateMap() {
        return new TreeMap<>(updates);
    }
    
    private DataEvent(DataManager<?> _src, int _op, Map<Object, Object> _updates) {
        if (_src == null) {
            throw new IllegalArgumentException("DataEvent: null given as source");
        } else if (_op < ADD_SINGLE || _op > UPDATE_MULTIPLE) {
            throw new IllegalArgumentException("DataEvent: unknown value given for operation.");
        } else if (_updates == null) {
            throw new IllegalArgumentException("DataEvent: null given as update map");
        }
        src = _src;
        op = _op;
        updates = _updates;
    }
}
