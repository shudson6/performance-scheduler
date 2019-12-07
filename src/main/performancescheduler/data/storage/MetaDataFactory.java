package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.Objects;

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
    
    public MetaFeature copyMetaData(MetaFeature toCopy, Feature toWrap) {
        Objects.requireNonNull(toCopy, "MetaDataFactory: MetaFeature toCopy must be non-null.");
        return new MetaFeature(toWrap, toCopy.getUuid(), toCopy.getCreatedTimestamp(), LocalDateTime.now());
    }
    
    public MetaPerformance newMetaPerformance(Performance toWrap) {
        return new MetaPerformance(toWrap, idGenerator.generateUUID(), LocalDateTime.now(), null);
    }
    
    public MetaPerformance copyMetaData(MetaPerformance toCopy, Performance toWrap) {
        Objects.requireNonNull(toCopy, "MetaDataFactory: MetaPerformance toCopy must be non-null.");
        return new MetaPerformance(toWrap, toCopy.getUuid(), toCopy.getCreatedTimestamp(), LocalDateTime.now());
    }
    
    private MetaDataFactory() {}
}
