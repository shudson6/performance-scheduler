package performancescheduler.data.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Test;

public class MetaWrapperTest {
	static UUID uuid = new UUID(0xabcdef1212341abcL, 0x9abcdef0234bcde7L);
	static LocalDateTime time = LocalDateTime.of(2020, 2, 13, 17, 00);

	@Test
	public void nullWrappedEqualsNullWrapped() {
		assertTrue(new MetaWrapper<Object>(null, uuid, time, time)
				.equals(new MetaWrapper<Object>(null, uuid, time, time)));
	}
	
	@Test
	public void nullWrappedNotEqualsNonNullWrapped() {
	    assertFalse(new MetaWrapper<String>(null, uuid, time, null)
	            .equals(new MetaWrapper<String>("Foo", uuid, time, null)));
	}
	
	@SuppressWarnings("unlikely-arg-type")
    @Test
	public void nullWrappedNotEqualsPlainNonNull() {
	    assertFalse(new MetaWrapper<String>(null, uuid, time, null).equals("foo"));
	}
	
	@Test
	public void nullToStringGivesNullInString() {
		assertEquals(MetaWrapper.NULLSTR, new MetaWrapper<Object>(null, uuid, time, null).toString());
	}
}
