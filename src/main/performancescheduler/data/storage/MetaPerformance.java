package performancescheduler.data.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

public class MetaPerformance extends MetaWrapper<Performance> implements Performance {

    MetaPerformance(Performance toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        super(toWrap, id, createTime, changeTime);
    }

    @Override
    public int compareTo(Performance o) {
    	if (wrapped != null) {
    		return wrapped.compareTo(o);
    	}
    	// wrapped is null
    	if (o instanceof MetaPerformance) {
    		return (((MetaPerformance) o).wrapped == null) ? 0 : 1;
    	} else {
    		return (o == null) ? 0 : 1;
    	}
    }

    @Override
    public LocalDate getDate() {
        return (wrapped != null) ? wrapped.getDate() : null;
    }

    @Override
    public LocalTime getTime() {
        return (wrapped != null) ? wrapped.getTime() : null;
    }

    @Override
    public LocalDateTime getDateTime() {
        return (wrapped != null) ? wrapped.getDateTime() : null;
    }

    @Override
    public int getAuditorium() {
        return (wrapped != null) ? wrapped.getAuditorium() : -1;
    }

    @Override
    public Feature getFeature() {
        return (wrapped != null) ? wrapped.getFeature() : null;
    }

    @Override
    public int getSeating() {
        return (wrapped != null) ? wrapped.getSeating() : 0;
    }

    @Override
    public int getCleanup() {
        return (wrapped != null) ? wrapped.getCleanup() : 0;
    }

    @Override
    public int getTrailers() {
        return (wrapped != null) ? wrapped.getTrailers() : 0;
    }

    @Override
    public int length() {
        return (wrapped != null) ? wrapped.length() : 0;
    }
}
