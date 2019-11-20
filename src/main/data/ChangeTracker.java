package main.data;

import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangeTracker<T extends Hashable> {
    private static MessageDigest sha256;
    static {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            sha256 = null;
            System.err.println("Caught NoSuchAlgorithmException when initializing SHA-256 digest. "
                    + "This indicates a problem with the JVM. Aborting program.");
            System.exit(0);
        }
    }
    
    private final State tail;
    private final byte[] hash;
    private final String hexString;
    private State current;
    private int count = 0;
    
    protected ChangeTracker(T item) {
        if (item == null) {
            throw new IllegalArgumentException("ChangeTracker: item cannot be null.");
        }
        tail = new State(item, null);
        current = tail;
        hash = sha256.digest(item.toByteBuffer().array());
        hexString = byteArrayToHexString(hash);
    }
    
    public final byte[] getHash() {
        return hash;
    }
    
    public final String getHumanReadableHash() {
        return hexString;
    }
    
    public T getCurrentState() {
        return current.state;
    }
    
    public void addChange(T item) {
        current.next = new State(item, current);
        current = current.next;
        count++;
    }
    
    public void revert() {
        if (current != tail) {
            current = current.prev;
            count--;
        }
    }
    
    public int changeCount() {
        return count;
    }
    
    public boolean hasChanged() {
        return count == 0;
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
    
    private class State {
        final T state;
        final State prev;
        State next;
        
        State(T item, State _prev) {
            state = item;
            prev = _prev;
            next = null;
        }
    }
}
