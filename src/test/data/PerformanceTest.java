package test.data;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.time.LocalDateTime;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.data.Auditorium;
import main.data.Feature;
import main.data.FeatureImpl;
import main.data.Location;
import main.data.Performance;
import main.data.PerformanceImpl;
import main.data.Rating;

public class PerformanceTest {
    Feature feature;
    Location location;
    LocalDateTime dateTime;
    Matcher<String> matchPI;
    
    @Before
    public void setUpBefore() {
        feature = FeatureImpl.getInstance("Foobar", Rating.NR, 90, false, false, false, false);
        location = Auditorium.getInstance(1, null, false, 100);
        dateTime = LocalDateTime.now();
        matchPI = CoreMatchers.containsString("PerformanceImpl");
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNullFeature() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(matchPI).and(containsString("feature")));
        PerformanceImpl.getInstance(null, dateTime, location);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNullDateTime() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(matchPI).and(containsString("date")));
        PerformanceImpl.getInstance(feature, null, location);
    }
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNoLocation() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(both(matchPI).and(containsString("location")));
        PerformanceImpl.getInstance(feature, dateTime, null);
    }

    @Test
    public void testGetters() {
        Performance p = PerformanceImpl.getInstance(feature, dateTime, location);
        assertEquals(feature, p.getFeature());
        assertEquals(dateTime, p.getDateTime());
        assertEquals(location, p.getLocation());
        assertEquals(dateTime.toLocalDate(), p.getDate());
        assertEquals(dateTime.toLocalTime(), p.getTime());
        String str = String.format("%s | %s, %s | %s", feature.getTitle(), dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), location.getName());
        assertEquals(str, p.toString());
    }
}
