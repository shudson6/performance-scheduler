package main.data;

/**
 * Interface for where--typically, in which auditorium--a performance will take place.
 *  
 * @author Steven Hudson
 */
public interface Location {
    /**
     * @return the name associated with this location
     */
    String getName();
    /**
     * @return {@code true} if this location can show 3D features
     */
    boolean is3dCapable();
    /**
     * @return the number of seats at this location
     */
    int getSeatCount();
}