package main.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractUniqueDataItem implements UniqueDataItem {
    private static MessageDigest SHA256;
    static {
        try {
            SHA256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            SHA256 = null;
            System.err.println("Caught NoSuchAlgorithmException when initializing SHA-256 digest. "
                    + "This indicates a problem with the JVM. Aborting program.");
            System.exit(0);
        }
    }
    
    private final byte[] hash;
    private final String hexString;
    
    protected AbstractUniqueDataItem(ByteBuffer buf) {
        if (buf == null) {
            // TODO
        }
        hash = SHA256.digest(buf.array());
        hexString = byteArrayToHexString(hash);
    }
    
    public final byte[] getHash() {
        return hash;
    }
    
    public final String getHumanReadableHash() {
        return hexString;
    }
    
    public static final String byteArrayToHexString(byte[] hash) {
        String hexChars = "0123456789abcdef";
        CharBuffer buf = CharBuffer.allocate(hash.length * 2);
        for (int i = 0; i < hash.length; i++) {
            buf.put(hexChars.charAt((hash[i] >> 4) & 0x0f));
            buf.put(hexChars.charAt(hash[i] & 0x0f));
        }
        return new String(buf.array());
    }
}
