package performancescheduler.data;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AuditoriumTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void shouldThrowIllegalArgumentExceptionNoSeats() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.containsString("Auditorium"));
        Auditorium.getInstance(0, null, false, 0);
    }

    @Test
    public void testGettersAndCoverage() {
        Auditorium a;
        a = Auditorium.getInstance(1, null, false, 100);
        assertEquals(1, a.getNumber());
        assertEquals("Auditorium 1", a.getName());
        assertEquals(a.getName(), a.toString());
        assertEquals(100, a.getSeatCount());
        assertEquals(false, a.is3dCapable());
        
        a = Auditorium.getInstance(2, "Foo!", true, 120);
        assertEquals(2, a.getNumber());
        assertEquals("Foo!", a.getName());
        assertEquals(true, a.is3dCapable());
        assertEquals(120, a.getSeatCount());
        
        a = Auditorium.getInstance(3, "", false, 150);
        assertEquals("Auditorium 3", a.getName());
    }
}
