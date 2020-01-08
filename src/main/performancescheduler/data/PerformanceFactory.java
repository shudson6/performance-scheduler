package performancescheduler.data;

import java.time.LocalDateTime;

/**
 * Class used to instantiate Performances.
 * 
 * @author Steven Hudson
 */
public class PerformanceFactory {
    
    public static PerformanceFactory newFactory() {
        return new PerformanceFactory();
    }
    
    /**
     * Create a new {@link Performance} instance.
     * @param feature the Feature to be shown
     * @param dateTime the date and time of showtime
     * @param auditorium number of the auditorium where it will show
     * @param seating seating time before the show, in minutes
     * @param cleanup cleanup time after the show, in minutes
     * @param trailers length of trailer pack, in minutes
     * @return a Performance instance
     */
    public Performance createPerformance(Feature feature, LocalDateTime dateTime, int auditorium, int seating,
            int cleanup, int trailers) {
        return new PerformanceImpl(feature, dateTime, auditorium, seating, cleanup, trailers);
    }
    
    private PerformanceFactory() {}
}
