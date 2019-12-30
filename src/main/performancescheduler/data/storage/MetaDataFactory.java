package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.util.UUIDGenerator;

public class MetaDataFactory {
    private UUIDGenerator idGenerator = new UUIDGenerator();
    
    public static MetaDataFactory newFactory() {
        return new MetaDataFactory();
    }
    
    public MetaFeature newMetaFeature(Feature toWrap) {
        return new MetaFeature(toWrap, idGenerator.generateUUID(), LocalDateTime.now(), null);
    }
    
    public MetaFeature newMetaFeature(Feature toWrap, UUID uuid, LocalDateTime created, LocalDateTime changed) {
        return new MetaFeature(toWrap, uuid, created, changed);
    }
    
    public MetaFeature copyMetaData(MetaFeature toCopy, Feature toWrap) {
        Objects.requireNonNull(toCopy, "MetaDataFactory: MetaFeature toCopy must be non-null.");
        return new MetaFeature(toWrap, toCopy.getUuid(), toCopy.getCreatedTimestamp(), LocalDateTime.now());
    }
    
    public MetaPerformance newMetaPerformance(Performance toWrap) {
        return new MetaPerformance(toWrap, idGenerator.generateUUID(), LocalDateTime.now(), null);
    }
    
    public MetaPerformance newMetaPerformance(Performance toWrap, UUID uuid, LocalDateTime created, 
            LocalDateTime changed) {
        return new MetaPerformance(toWrap, uuid, created, changed);
    }
    
    public MetaPerformance copyMetaData(MetaPerformance toCopy, Performance toWrap) {
        Objects.requireNonNull(toCopy, "MetaDataFactory: MetaPerformance toCopy must be non-null.");
        return new MetaPerformance(toWrap, toCopy.getUuid(), toCopy.getCreatedTimestamp(), LocalDateTime.now());
    }
    
    private MetaDataFactory() {}
}
