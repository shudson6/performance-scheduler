package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base wrapper class for keeping metadata on objects. Expected use is to track changes to uniquely identifiable objects
 * stored using immutable data types to facilitate use of a database storage system.
 * 
 * <p>Note that this class does allow wrapping {@code null}; subclasses need to be aware of this and avoid
 * {@code NullPointerException}s.
 * 
 * @param T the type of object being wrapped
 * @author Steven Hudson
 */
public class MetaWrapper<T> {
	public static final String NULLSTR = "[null]";
	
    private final UUID uuid;
    private final LocalDateTime created;
    private final LocalDateTime changed;
    protected T wrapped;
    
    MetaWrapper(T toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        Objects.requireNonNull(id, "MetaWrapper: id must be non-null.");
        Objects.requireNonNull(createTime, "MetaWrapper: creation time must be non-null.");
        
        wrapped = toWrap;
        uuid = id;
        created = createTime;
        changed = (changeTime != null) ? changeTime : createTime;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public LocalDateTime getCreatedTimestamp() {
        return created;
    }
    
    public LocalDateTime getChangedTimestamp() {
        return changed;
    }
    
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof MetaWrapper) {
            return uuid.equals(((MetaWrapper<?>) o).uuid) 
                    && created.equals(((MetaWrapper<?>) o).created)
                    && changed.equals(((MetaWrapper<?>) o).changed) 
                    && ((wrapped != null) ? wrapped.equals(((MetaWrapper<?>) o).wrapped)
                            : (((MetaWrapper<?>) o).wrapped == null));
        }
        return (wrapped == null) ? o == null : wrapped.equals(o);
    }
    
    @Override
    public String toString() {
        return (wrapped == null) ? NULLSTR : wrapped.toString();
    }
}
