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
     * @return the number of the auditorium in which this performance is scheduled
     */
    public int getAuditorium();
    /**
     * @return the feature being presented
     */
    public Feature getFeature();
    /**
     * Get the guaranteed time, in minutes, the auditorium must be empty before this performance.
     * This time begins after the cleanup time of the previous performance, if any.
     * @return seating time
     */
    public int getSeating();
    /**
     * Get the guaranteed time, in minutes, required after this performance before 
     * the seating time of another may begin.
     * @return cleanup time
     */
    public int getCleanup();
    /**
     * @return length, in minutes, of the trailer pack
     */
    public int getTrailers();
    /**
     * Get the total length of this performance. This is the total of the feature length plus seating and cleanup times
     * plus the length of trailers.
     */
    public int length();
}
