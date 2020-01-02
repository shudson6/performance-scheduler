package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.UUID;

import performancescheduler.data.Performance;
import performancescheduler.data.storage.MetaPerformance;

public class TestMetaPerformance extends MetaPerformance {
    public TestMetaPerformance(Performance toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        super(toWrap, id, createTime, changeTime);
    }
}
