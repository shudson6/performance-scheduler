package performancescheduler.data.storage;

import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureWrapper;
import performancescheduler.data.Rating;

public final class UuidableFeature extends FeatureWrapper {
    private final UUID uuid;
    
    UuidableFeature(Feature ftr, UUID id) {
        super(ftr);
        if (id == null) {
            throw new NullPointerException("UuidableFeature: null UUID passed to constructor.");
        }
        uuid = id;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof UuidableFeature) {
            return super.equals(o) && uuid.equals(((UuidableFeature) o).getUuid());
        } else {
            return super.equals(o);
        }
    }
}
