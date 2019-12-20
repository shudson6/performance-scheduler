package performancescheduler.data.storage.sql;

import java.time.LocalDateTime;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.storage.MetaFeature;

public class TestMetaFeature extends MetaFeature {
    public TestMetaFeature(Feature toWrap, UUID id, LocalDateTime create, LocalDateTime change) {
        super(toWrap, id, create, change);
    }
}
