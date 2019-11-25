package main.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;

public class UUIDGenerator {
    public static final Instant base = LocalDateTime.of(1582, 10, 15, 0, 0).toInstant(ZoneOffset.UTC);
    public static final Random RANDOM = initRandom();
    private static Instant last = null;
    private static short sequence = (short)RANDOM.nextInt();
    
    public UUID generateUUID() {
        long hi = 0;
        long lo = 0;
        byte data[];
        /* first, get the timestamp and put it into the high-order long
         * the timestamp will be 64 bits of which the highest 4 are ignored (and should be 0)
         */
        long ts = getTimestamp();
        hi |= ts << 32;
        hi |= (ts & 0x0000ffff00000000l) >> 16;
        hi |= (ts & 0x0fff000000000000l) >> 48;
        // version
        hi |= 0x0000000000001000l;
        /* next, get the node address and put it into the low-order long
         * along with the variant (10) and clock sequence
         */
        lo = getNodeAddress();
        lo |= (long) sequence << 48;
        // put the variant
        lo &= 0x3fffffffffffffffl;
        lo |= 0x8000000000000000l;
        
        return new UUID(hi, lo);
    }
    
    private long getTimestamp() {
        Instant now = Instant.now();
        long _100nanos = Duration.between(now, base).dividedBy(100l).toNanos();
        if (now.equals(last)) {
            sequence++;
        } else {
            last = now;
        }
        return _100nanos;
    }

    private long getNodeAddress() {
        ByteBuffer naBuf = ByteBuffer.allocate(8);
        naBuf.put(new byte[] { 0x00, 0x00 });
        byte node[] = null;
        try {
            Enumeration<NetworkInterface> netInts = NetworkInterface.getNetworkInterfaces();
            if (netInts != null) {
                node = netInts.asIterator().next().getHardwareAddress();
            }
        } catch (SocketException ex) {
            System.err.print("UUIDGenerator: caught SocketException when generating node address. ");
            System.err.println(ex.getMessage());
        }
        // if we have a legitimate 48-bit address, add it it.
        if (node == null || node.length != 6) {
            // if we're here, we have to go the random route
            node = new byte[6];
            RANDOM.nextBytes(node);
            // make sure the high-order bit is set
            node[0] |= 0x80;
        }
        naBuf.put(node);
        naBuf.rewind();
        return naBuf.getLong();
    }
    
    private static Random initRandom() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException ex) {
            System.out.print("NoSuchAlgorithmException caught when initializing SecureRandom: ");
            System.out.println(ex.getMessage());
            return new Random(System.nanoTime());
        }
    }
}
