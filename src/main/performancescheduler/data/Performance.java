package performancescheduler.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Interface for classes that provide data about a performance.
 * 
 * @author Steven Hudson
 */
public interface Performance extends Comparable<Performance> {
    /**
     * @return the date of the performance
     */
    public LocalDate getDate();
    /**
     * @return the time of the performance
     */
    public LocalTime getTime();
    /**
     * @return the {@link LocalDateTime} member of this instance
     */
    public LocalDateTime getDateTime();
    /**
     * @return the location of the performance
     */
    public Auditorium getAuditorium();
    /**
     * @return the feature being presented
     */
    public Feature getFeature();
}
