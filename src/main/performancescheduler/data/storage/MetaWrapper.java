package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class MetaWrapper<T> {
    private final UUID uuid;
    private final LocalDateTime created;
    private final LocalDateTime changed;
    protected T wrapped;
    
    MetaWrapper(T toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        Objects.requireNonNull(toWrap, "MetaWrapper: wrapped data must be non-null.");
        Objects.requireNonNull(id, "MetaWrapper: id must be non-null.");
        Objects.requireNonNull(createTime, "MetaWrapper: creation time must be non-null.");
        
        wrapped = toWrap;
        uuid = id;
        created = createTime;
        changed = (changeTime != null) ? changeTime : createTime;
    }
    
    UUID getUuid() {
        return uuid;
    }
    
    LocalDateTime getCreatedTimestamp() {
        return created;
    }
    
    LocalDateTime getChangedTimestamp() {
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
                    && wrapped.equals(((MetaWrapper<?>) o).wrapped);
        }
        return wrapped.equals(o);
    }
    
    @Override
    public String toString() {
        return wrapped.toString();
    }
}
