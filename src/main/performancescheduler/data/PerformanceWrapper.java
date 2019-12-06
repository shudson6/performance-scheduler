package performancescheduler.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PerformanceWrapper extends Wrapper<Performance> implements Performance {

    protected PerformanceWrapper(Performance toWrap) {
        super(toWrap);
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
