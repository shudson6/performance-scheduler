package performancescheduler.data.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class MetaPerformance extends MetaWrapper<Performance> implements Performance {

    protected MetaPerformance(Performance toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        super(toWrap, id, createTime, changeTime);
    }

    @Override
    public int compareTo(Performance o) {
        return wrapped.compareTo(o);
    }

    @Override
    public LocalDate getDate() {
        return wrapped.getDate();
    }

    @Override
    public LocalTime getTime() {
        return wrapped.getTime();
    }

    @Override
    public LocalDateTime getDateTime() {
        return wrapped.getDateTime();
    }

    @Override
    public Auditorium getAuditorium() {
        return wrapped.getAuditorium();
    }

    @Override
    public Feature getFeature() {
        return wrapped.getFeature();
    }
}
