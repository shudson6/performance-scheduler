package performancescheduler.util;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import performancescheduler.util.UUIDGenerator;

public class UUIDGeneratorTest {

    @Test
    public void test() {
        UUIDGenerator uuidGen = new UUIDGenerator();
        UUID array[] = new UUID[10];
        for (int i = 0; i < 10; i++) {
            UUID uuid = uuidGen.generateUUID();
            System.out.println(uuid.toString());
            assertEquals("Wrong version: ", 1, uuid.version());
            assertEquals("Wrong variant: ", 0b10, uuid.variant());
            array[i] = uuid;
        }
        assertTrue(allUnique(array));
    }
    
    private static boolean allUnique(UUID uuids[]) {
        for (int i = 0; i < uuids.length; i++) {
            for (int j = i + 1; j < uuids.length; j++) {
                if (uuids[i].equals(uuids[j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
