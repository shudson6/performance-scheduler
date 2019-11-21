package main.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Basic {@link Performance} implementation. Create an instance using
 * {@link #getInstance(Feature, LocalDateTime, Location)}.
 * 
 * @author Steven Hudson
 */
public class PerformanceImpl implements Performance {
    private final LocalDateTime dateTime;
    private final Feature feature;
    private final Location location;
    
    public static Performance getInstance(Feature feature, LocalDateTime dateTime, Location location) {
        return new PerformanceImpl(feature, dateTime, location);
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
    public Location getLocation() {
        return location;
    }

    @Override
    public Feature getFeature() {
        return feature;
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s, %s | %s", feature.getTitle(), dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), location.getName());
    }
    
    private PerformanceImpl(Feature f, LocalDateTime d, Location l) {
        if (f == null) {
            throw new IllegalArgumentException("PerformanceImpl: feature must not be null.");
        } else if (d == null) {
            throw new IllegalArgumentException("PerformanceImpl: date/time must not be null.");
        } else if (l == null) {
            throw new IllegalArgumentException("PerformanceImpl: location must not be null.");
        }
        feature = f;
        dateTime = d;
        location = l;
    }
}
