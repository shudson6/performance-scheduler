package main.java.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Basic {@link Performance} implementation.
 * 
 * @author Steven Hudson
 */
class PerformanceImpl implements Performance {
    private final LocalDateTime dateTime;
    private final Feature feature;
    private final Auditorium auditorium;
    
    /**
     * Create a new instance.
     * @param f Feature
     * @param d date and time
     * @param l auditorium
     */
    public PerformanceImpl(Feature f, LocalDateTime d, Auditorium l) {
        if (f == null) {
            throw new IllegalArgumentException("PerformanceImpl: feature must not be null.");
        } else if (d == null) {
            throw new IllegalArgumentException("PerformanceImpl: date/time must not be null.");
        } else if (l == null) {
            throw new IllegalArgumentException("PerformanceImpl: location must not be null.");
        }
        feature = f;
        dateTime = d;
        auditorium = l;
    }

    @Override
    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    @Override
    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public Auditorium getAuditorium() {
        return auditorium;
    }

    @Override
    public Feature getFeature() {
        return feature;
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s, %s | %s", feature.getTitle(), dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), auditorium.getName());
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Performance) {
            Performance p = (Performance) o;
            // using == because this does require they point to the same instance
            return this.getFeature() == p.getFeature() 
                    && this.getAuditorium().getNumber() == p.getAuditorium().getNumber()
                    && this.getDateTime().equals(p.getDateTime());
        }
        return false;
    }
    
    @Override
    public int compareTo(Performance p) {
        if (p == null) {
            throw new NullPointerException("PerformanceImpl.compareTo: received null parameter");
        }
        int result = this.getFeature().compareTo(p.getFeature());
        if (result == 0) {
            result = Integer.compare(this.getAuditorium().getNumber(), p.getAuditorium().getNumber());
        }
        if (result == 0) {
            result = this.getDateTime().compareTo(p.getDateTime());
        }
        return result;
    }
}
