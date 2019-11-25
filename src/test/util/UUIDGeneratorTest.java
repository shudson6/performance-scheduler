package test.util;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import main.util.UUIDGenerator;

public class UUIDGeneratorTest {

    @Test
    public void test() {
        UUIDGenerator uuidGen = new UUIDGenerator();
        for (int i = 0; i < 10; i++) {
            UUID uuid = uuidGen.generateUUID();
            System.out.println(uuid.toString());
            assertEquals("Wrong version: ", 1, uuid.version());
            assertEquals("Wrong variant: ", 0b10, uuid.variant());
        }
    }

}
