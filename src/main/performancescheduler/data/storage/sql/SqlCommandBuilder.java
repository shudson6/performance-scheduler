package performancescheduler.data.storage.sql;

import java.util.ArrayList;
import java.util.List;

abstract class SqlCommandBuilder<T> {
    private List<T> data = new ArrayList<>();
    
    public void clear() {
        data.clear();
    }
    
    public boolean add(T toAdd) {
        return data.add(toAdd);
    }
    
    public String getCommand() {
        return buildCommand();
    }
    
    protected final List<T> getData() {
        return data;
    }
    
    protected abstract String buildCommand();
}
