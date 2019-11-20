package main.data;

import java.nio.ByteBuffer;

public interface Hashable {
    /**
     * Creates a {@link ByteBuffer} instance containing identifying members of this instance.
     * The members used in the buffer should be the same as those used by 
     * {@link Object#equals(Object)} and {@link Object#hashCode()}.
     * 
     * @return a {@code ByteBuffer} based on instance data
     */
    public ByteBuffer toByteBuffer();
}
