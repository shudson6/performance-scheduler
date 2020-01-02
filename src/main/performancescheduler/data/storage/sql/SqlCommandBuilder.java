package performancescheduler.data.storage.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class used by SQL classes to generate SQL commands.
 * @author Steven Hudson
 *
 * @param <T> the type of data on which the command will be built
 */
abstract class SqlCommandBuilder<T> {
    private List<T> data = new ArrayList<>();
    
    /**
     * Removes all data from the internal structure.
     */
    public void clear() {
        data.clear();
    }
    
    /**
     * Adds an element to this instance.
     * 
     * @param toAdd the element to add
     * @return {@code true} if the internal data structure changed as a result of the operation
     */
    public boolean add(T toAdd) {
        return data.add(toAdd);
    }
    
    /**
     * Get the command string generated for this instance's assigned data.
     * @return new {@code String} containing generated command(s)
     */
    public String getCommand() {
        return buildCommand();
    }
    
    protected final List<T> getData() {
        return data;
    }
    
    protected abstract String buildCommand();
}
