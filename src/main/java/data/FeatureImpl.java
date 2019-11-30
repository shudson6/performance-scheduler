package main.java.data;

/**
 * Basic {@link Feature} implementation. Create an instance using
 * {@link #getInstance(String, Rating, int, boolean, boolean, boolean, boolean)}.
 * 
 * @author Steven Hudson
 */
public class FeatureImpl implements Feature {
    protected static final byte CC = 0x01;
    protected static final byte OC = 0x02;
    protected static final byte DA = 0x04;
    
    private final String title;
    private final Rating rating;
    private final int runtime;
    private final boolean is3D;
    private final boolean hasCC;
    private final boolean hasOC;
    private final boolean hasDA;
    
    /**
     * Get a new instance. Must have a valid (ie non-null) title and rating as well as a 
     * positive runtime.
     * @param title the name of the movie; not {@code null}
     * @param rating the movie's rating
     * @param runtime the runtime in minutes
     * @param is3D {@code true} if the feature is in 3D
     * @param hasCC {@code true} if the feature has Closed Captions
     * @param hasOC {@code true} if the feature has Open Captions
     * @param hasDA {@code true} if the feature has Descriptive Audio
     * @return a new {@link FeatureImpl} instance
     */
    public static Feature getInstance(String title, Rating rating, int runtime, boolean is3D,
            boolean hasCC, boolean hasOC, boolean hasDA) {
        return new FeatureImpl(title, rating, runtime, is3D, hasCC, hasOC, hasDA);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Rating getRating() {
        return rating;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getRuntime() {
        return runtime;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean is3d() {
        return is3D;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasClosedCaptions() {
        return hasCC;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasOpenCaptions() {
        return hasOC;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDescriptiveAudio() {
        return hasDA;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Feature) {
            Feature ftr = (Feature) o;
            return this.getTitle().equalsIgnoreCase(ftr.getTitle()) && this.is3d() == ftr.is3d()
                    && this.hasClosedCaptions() == ftr.hasClosedCaptions()
                    && this.hasOpenCaptions() == ftr.hasOpenCaptions()
                    && this.hasDescriptiveAudio() == ftr.hasDescriptiveAudio()
                    && this.getRating().equals(ftr.getRating())
                    && this.getRuntime() == ftr.getRuntime();
        }
        return false;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Feature ftr) {
        if (ftr == null) {
            throw new NullPointerException("FeatureImpl.compareTo: received null parameter.");
        }
        int result = this.getTitle().compareToIgnoreCase(ftr.getTitle());
        if (result == 0) {
            result = Boolean.compare(this.is3d(), ftr.is3d());
        }
        if (result == 0) {
            result = Boolean.compare(this.hasClosedCaptions(), ftr.hasClosedCaptions());
        }
        if (result == 0) {
            result = Boolean.compare(this.hasOpenCaptions(), ftr.hasOpenCaptions());
        }
        if (result == 0) {
            result = Boolean.compare(this.hasDescriptiveAudio(), ftr.hasDescriptiveAudio());
        }
        if (result == 0) {
            result = this.getRating().compareTo(ftr.getRating());
        }
        if (result == 0) {
            result = Integer.compare(this.getRuntime(), ftr.getRuntime());
        }
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("%s%s %dmin %s %s%s%s", getTitle(), 
                is3d() && !title.endsWith("3D") ? " 3D" : "", getRuntime(), getRating().toString(),
                hasClosedCaptions() ? "CC " : "", hasOpenCaptions() ? "OC " : "",
                hasDescriptiveAudio() ? "DA" : "");
    }
    
    private FeatureImpl(String t, Rating r, int m, boolean _3D, boolean cc, boolean oc, boolean da) {
        checkArgs(t, r, m);
        title = t;
        rating = r;
        runtime = m;
        is3D = _3D;
        hasCC = cc;
        hasOC = oc;
        hasDA = da;
    }
    
    private static void checkArgs(String t, Rating r, int m) {
        if (t == null) {
            throw new IllegalArgumentException("FeatureImpl: Title cannot be null.");
        } else if (t.isEmpty()) {
            throw new IllegalArgumentException("FeatureImpl: Title cannot be empty.");
        }
        if (r == null) {
            throw new IllegalArgumentException("FeatureImpl: Rating cannot be null.");
        }
        if (m < 1) {
            throw new IllegalArgumentException("FeatureImpl: runtime must be >0.");
        }
    }
}
