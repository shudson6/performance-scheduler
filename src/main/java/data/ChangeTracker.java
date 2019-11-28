package main.java.data;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for tracking changes to objects represented by immutable instances. When the object
 * enclosed by a ChangeTracker is replaced in its data structure, the new object should be added
 * as a change to the ChangeTracker as well. The ChangeTracker's state will return the appropriate
 * valid object from its {@link #getCurrentState()} method. All states that have been added to the
 * tracker are held in an internal doubly-linked list so that previous states may be restored by
 * the {@link #revert()} method.
 * <p>
 * The purpose of this class is to allow a uniquely identifiable object to change while retaining
 * both its identity and the immutability of its class. As a side effect, it could be useful in
 * an undo system.
 * 
 * @param <T> the class of data stored in this tracker
 * 
 * @author Steven Hudson
 */
public class ChangeTracker<T> {
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
    
    /**
     * Start tracking an item.
     * @param item the object to be tracked
     */
    public ChangeTracker(T item) {
        if (item == null) {
            throw new IllegalArgumentException("ChangeTracker: item cannot be null.");
        }
        tail = new State(item, null);
        current = tail;
        hash = sha256.digest(bytesFor(item));
        hexString = byteArrayToHexString(hash);
    }
    
    /**
     * @return the hash value used to identify this tracker
     */
    public final byte[] getHash() {
        return hash;
    }
    
    /**
     * @return {@code String} representation of the value returned by {@link #getHash()}
     */
    public final String getHumanReadableHash() {
        return hexString;
    }
   
    /**
     * @return the current state of the tracked object
     */
    public T getCurrentState() {
        return current.state;
    }
    
    /**
     * Updates the state of the tracked object by adding a new item as the head of
     * the internal list.
     * 
     * @param item the new state of the object
     */
    public void addChange(T item) {
        current.next = new State(item, current);
        current = current.next;
        count++;
    }
    
    /**
     * Moves the current state pointer back to the previous state.
     */
    public void revert() {
        if (current != tail) {
            current = current.prev;
            count--;
        }
    }
    
    /**
     * @return the number of state changes from the original state to current
     */
    public int changeCount() {
        return count;
    }
    
    /**
     * @return {@code true} if the current state is not the initial state
     */
    public boolean hasChanged() {
        return count != 0;
    }
    
    /**
     * Converts a byte array to a hexadecimal string.
     * @param hash the byte array
     * @return {@code String}
     */
    public static final String byteArrayToHexString(byte[] hash) {
        String hexChars = "0123456789abcdef";
        CharBuffer buf = CharBuffer.allocate(hash.length * 2);
        for (int i = 0; i < hash.length; i++) {
            buf.put(hexChars.charAt((hash[i] >> 4) & 0x0f));
            buf.put(hexChars.charAt(hash[i] & 0x0f));
        }
        return new String(buf.array());
    }
    
    private byte[] bytesFor(T item) {
        ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES + Long.BYTES);
        buf.putInt(item.hashCode());
        buf.putLong(System.nanoTime());
        return buf.array();
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
