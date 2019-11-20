package main.data;

import java.nio.ByteBuffer;
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
                    + "This indicates an impossible state. Aborting program.");
            System.exit(0);
        }
    }
    
    private final byte[] hash;
    
    protected AbstractUniqueDataItem(ByteBuffer buf) {
        if (buf == null) {
            // TODO
        }
        hash = SHA256.digest(buf.array());
    }
    
    public final byte[] getUniqueID() {
        return hash;
    }
}
