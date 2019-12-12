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
    private final Auditorium auditorium;
    
    /**
     * Create a new instance.
     * @param f Feature
     * @param d date and time not {@code null}
     * @param l auditorium not {@code null}
     * @throws NullPointerException if the time or auditorium are {@code null}
     */
    PerformanceImpl(Feature f, LocalDateTime d, Auditorium l) {
        Objects.requireNonNull(d, "PerformanceImpl: date/time must not be null.");
        Objects.requireNonNull(l, "PerformanceImpl: location must not be null.");
        
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
        String title = (feature != null) ? feature.getTitle() : "[null feature]";
        return String.format("%s | %s, %s | %s", title, dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), auditorium.getName());
    }
    
    @Override
    public int compareTo(Performance p) {
        Objects.requireNonNull(p, "PerformanceImpl.compareTo: received null parameter");
        
        int result;
        if (feature == null) {
            result = p.getFeature() == null ? 0 : -1;
        } else {
            result = p.getFeature() == null ? 1 : feature.compareTo(p.getFeature());
        }
        if (result == 0) {
            result = Integer.compare(this.auditorium.getNumber(), p.getAuditorium().getNumber());
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
            return result && this.getAuditorium().getNumber() == p.getAuditorium().getNumber()
                    && this.getDateTime().equals(p.getDateTime());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int rslt = 23 * ((feature != null) ? feature.hashCode() : 1);
        rslt = 23 * rslt + dateTime.hashCode();
        rslt = 23 * rslt + auditorium.hashCode();
        return rslt;
    }
}
