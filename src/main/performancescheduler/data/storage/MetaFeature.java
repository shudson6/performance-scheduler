package performancescheduler.data.storage;

import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureWrapper;
import performancescheduler.data.Rating;

class MetaFeature extends FeatureWrapper {
    private final UUID uuid;
    
    MetaFeature(Feature ftr, UUID id) {
        super(ftr);
        if (id == null) {
            throw new NullPointerException("MetaFeature: null UUID passed to constructor.");
        }
        uuid = id;
    }
    
    UUID getUuid() {
        return uuid;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof MetaFeature) {
            return super.equals(o) && uuid.equals(((MetaFeature) o).getUuid());
        } else {
            return super.equals(o);
        }
    }
    
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
