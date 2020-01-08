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
    private final int seating;
    private final int cleanup;
    private final int trailers;
    
    /**
     * Create a new instance.
     * @param f Feature
     * @param d date and time not {@code null}
     * @param aud auditorium number
     * @param seat seating time before the show, in minutes
     * @param clean cleanup time after the show, in minutes
     * @param preview length of trailer pack, in minutes
     * @throws NullPointerException if the time is {@code null}
     * @throws IllegalArgumentException if any integer parameter is negative
     */
    PerformanceImpl(Feature f, LocalDateTime d, int aud, int seat, int clean, int preview) {
        Objects.requireNonNull(d, "PerformanceImpl: date/time must not be null.");
        if (aud < 0 || seat < 0 || clean < 0 || preview < 0) {
        	throw new IllegalArgumentException("PerformanceImpl: number must be positive.");
        }
        
        feature = f;
        dateTime = d;
        auditorium = aud;
        seating = seat;
        cleanup = clean;
        trailers = preview;
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
    public int getSeating() {
        return seating;
    }

    @Override
    public int getCleanup() {
        return cleanup;
    }

    @Override
    public int getTrailers() {
        return trailers;
    }
    
    @Override
    public String toString() {
        String title = (feature != null) ? feature.getTitle() : "[null feature]";
        return String.format("%s | %s, %s | aud %d", title, dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), auditorium);
    }
    
    /**
     * Compares this performance to another. The natural ordering is defined first by the ordering of the associated
     * {@link Feature}, then by auditorium number, then time.
     * 
     * <p>Note that this ordering is not entirely consistent with {@link #equals()}, as it does not consider the
     * seating, cleanup, or trailer times, leaving the order of instances which differ only in these attributes as
     * undefined.
     */
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
            return result && this.auditorium == p.getAuditorium() && this.dateTime.equals(p.getDateTime())
                    && this.seating == p.getSeating() && this.cleanup == p.getCleanup()
                    && this.trailers == p.getTrailers();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int rslt = 23 * ((feature != null) ? feature.hashCode() : 1);
        rslt = 23 * rslt + dateTime.hashCode();
        rslt = 23 * rslt + auditorium;
        rslt = 23 * rslt + cleanup;
        rslt = 23 * rslt + seating;
        rslt = 23 * rslt + trailers;
        return rslt;
    }
}
