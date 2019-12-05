package main.java.data;

import java.time.LocalDateTime;

/**
 * Class used to instantiate Performances.
 * 
 * @author Steven Hudson
 */
public class PerformanceFactory {
    /**
     * Create a new {@link Performance} instance.
     * @param feature the Feature to be shown
     * @param dateTime the date and time of showtime
     * @param auditorium the auditorium where it will show
     * @return a Performance instance
     */
    public Performance createPerformance(Feature feature, LocalDateTime dateTime, Auditorium auditorium) {
        return new PerformanceImpl(feature, dateTime, auditorium);
    }
}
