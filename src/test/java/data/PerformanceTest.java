package test.java.data;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.time.LocalDateTime;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import main.java.data.Auditorium;
import main.java.data.Feature;
import main.java.data.FeatureImpl;
import main.java.data.Performance;
import main.java.data.PerformanceImpl;
import main.java.data.Rating;

public class PerformanceTest {
    Feature feature;
    Auditorium location;
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
        assertEquals(location, p.getAuditorium());
        assertEquals(dateTime.toLocalDate(), p.getDate());
        assertEquals(dateTime.toLocalTime(), p.getTime());
        String str = String.format("%s | %s, %s | %s", feature.getTitle(), dateTime.toLocalDate().toString(),
                dateTime.toLocalTime().toString(), location.getName());
        assertEquals(str, p.toString());
    }
}
