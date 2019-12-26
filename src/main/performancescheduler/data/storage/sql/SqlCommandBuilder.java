package performancescheduler.data.storage.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class SqlCommandBuilder<T> {
    private List<T> data = new ArrayList<>();
    
    public void clear() {
        data.clear();
    }
    
    public boolean add(T toAdd) {
        return data.add(toAdd);
    }
    
    public boolean addAll(Collection<T> toAdd) {
        boolean result = false;
        for (T t : toAdd) {
            result |= add(t);
        }
        return result;
    }
    
    public String getCommand() {
        return buildCommand();
    }
    
    protected final List<T> getData() {
        return data;
    }
    
    protected abstract String buildCommand();
}
