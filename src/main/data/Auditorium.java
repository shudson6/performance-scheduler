package main.data;

/**
 * {@link Location} implementation which additionally includes an auditorium number.
 * 
 * @author Steven Hudson
 */
public class Auditorium implements Location {
    private final int number;
    private final String name;
    private final boolean is3d;
    private final int seats;
    
    /**
     * Get a new instance. If the {@code String name} is {@code null} or empty,
     * "Auditorium #" will be used, where '#' is the {@code number} parameter.
     * 
     * @param number this auditorium's identifying number
     * @param name this auditorium's name
     * @param is3d {@code true} if this auditorium can play 3D content
     * @param seats the number of seats in this auditorium
     * @return a new instance
     */
    public static Auditorium getInstance(int number, String name, boolean is3d, int seats) {
        return new Auditorium(number, name, is3d, seats);
    }
    
    /**
     * @return the number of this auditorium within a multiplex
     */
    public int getNumber() {
        return number;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean is3dCapable() {
        return is3d;
    }
    
    @Override
    public int getSeatCount() {
        return seats;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    private Auditorium(int nr, String nm, boolean _3d, int s) {
        if (s < 1) {
            throw new IllegalArgumentException("Auditorium: seat count must be a positive number.");
        }
        number = nr;
        name = (nm == null || nm.isEmpty()) ? "Auditorium " + nr : nm;
        is3d = _3d;
        seats = s;
    }
}
