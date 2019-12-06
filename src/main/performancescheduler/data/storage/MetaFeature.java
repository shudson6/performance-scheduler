package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureWrapper;
import performancescheduler.data.Rating;

class MetaFeature extends FeatureWrapper {
    private final UUID uuid;
    private final LocalDateTime created;
    private final LocalDateTime changed;
    
    MetaFeature(Feature feature, UUID id, LocalDateTime created, LocalDateTime changed) {
        super(feature);
        if (id == null) {
            throw new NullPointerException("MetaFeature: null UUID passed to constructor.");
        }
        if (created == null) {
            throw new NullPointerException("MetaFeature: creation timestamp must be non-null.");
        }
        if (changed == null) {
            changed = created;
        }
        uuid = id;
        this.created = created;
        this.changed = changed;
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
    public boolean equals(Object o) {
        if (o instanceof MetaFeature) {
            return super.equals(o) && uuid.equals(((MetaFeature) o).uuid)
                    && created.equals((MetaFeature) o).created && changed.equals((MetaFeature) o).changed;
        } else {
            return super.equals(o);
        }
    }
    
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
