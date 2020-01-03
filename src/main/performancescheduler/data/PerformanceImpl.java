package performancescheduler.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Basic {@link Performance} implementation.
 * 
 * <p> The {@code Feature} member of this class may be {@code null}.
 * 
 * @author Steven Hudson
 */
class PerformanceImpl implements Performance {
    private final LocalDateTime dateTime;
    private final Feature feature;
    private final int auditorium;
    
    /**
     * Create a new instance.
     * @param f Feature
     * @param d date and time not {@code null}
     * @param aud auditorium number
     * @throws NullPointerException if the time is {@code null}
     * @throws IllegalArgumentException if the auditorium is negative
     */
    PerformanceImpl(Feature f, LocalDateTime d, int aud) {
        Objects.requireNonNull(d, "PerformanceImpl: date/time must not be null.");
        if (aud < 0) {
        	throw new IllegalArgumentException("PerformanceImpl: auditorium number must be positive.");
        }
        
        feature = f;
        dateTime = d;
        auditorium = aud;
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
    public int getAuditorium() {
        return auditorium;
    }

    @Override
    public Feature getFeature() {
        return feature;
    }
    
    @Override
    public String toString() {
        String title = (feature != null) ? feature.getTitle() : "[null feature]";
        return String.format("%s | %s, %s | aud %d", title, dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), auditorium);
    }
    
    @Override
    public int compareTo(Performance p) {
        if (p == null) {
        	// defines a natural ordering in which null comes after all valid instances
        	return -1;
        }
        int result;
        if (feature == null) {
            result = p.getFeature() == null ? 0 : -1;
        } else {
            result = p.getFeature() == null ? 1 : feature.compareTo(p.getFeature());
        }
        if (result == 0) {
            result = Integer.compare(this.auditorium, p.getAuditorium());
        }
        if (result == 0) {
            result = this.dateTime.compareTo(p.getDateTime());
        }
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Performance) {
            Performance p = (Performance) o;
            boolean result = (feature == null) ? p.getFeature() == null : feature.equals(p.getFeature());
            return result && this.getAuditorium() == p.getAuditorium()
                    && this.getDateTime().equals(p.getDateTime());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int rslt = 23 * ((feature != null) ? feature.hashCode() : 1);
        rslt = 23 * rslt + dateTime.hashCode();
        rslt = 23 * rslt + auditorium;
        return rslt;
    }
}
