package performancescheduler.data.storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.Test;

public class MetaWrapperTest {
	static UUID uuid = new UUID(0xabcdef1212341abcL, 0x9abcdef0234bcde7L);
	static LocalDateTime time = LocalDateTime.of(2020, 2, 13, 17, 00);

	@Test
	public void nullWrappedEqualsNullObj() {
		assertTrue(new MetaWrapper<Object>(null, uuid, time, null).equals(null));
	}

	@Test
	public void nullWrappedEqualsNullWrapped() {
		assertTrue(new MetaWrapper<Object>(null, uuid, time, time)
				.equals(new MetaWrapper<Object>(null, uuid, time, time)));
	}
	
	@Test
	public void nullToStringGivesNullInString() {
		assertEquals(MetaWrapper.NULLSTR, new MetaWrapper<Object>(null, uuid, time, null).toString());
	}
}
