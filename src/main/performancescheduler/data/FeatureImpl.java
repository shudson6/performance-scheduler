package performancescheduler.data;

/**
 * Basic {@link Feature} implementation.
 * 
 * @author Steven Hudson
 */
class FeatureImpl implements Feature {
    protected static final byte CC = 0x01;
    protected static final byte OC = 0x02;
    protected static final byte DA = 0x04;
    
    private final String title;
    private final Rating rating;
    private final int runtime;
    private final boolean is3d;
    private final boolean hasCC;
    private final boolean hasOC;
    private final boolean hasDA;
    
    /**
     * Get a new instance. Must have a valid (ie non-null) title and rating as well as a 
     * positive runtime.
     * @param t the name of the movie; not {@code null}
     * @param r the movie's rating
     * @param m the runtime in minutes
     * @param _3D {@code true} if the feature is in 3D
     * @param cc {@code true} if the feature has Closed Captions
     * @param oc {@code true} if the feature has Open Captions
     * @param da {@code true} if the feature has Descriptive Audio
     * @return a new {@link FeatureImpl} instance
     */    
    public FeatureImpl(String t, Rating r, int m, boolean _3D, boolean cc, boolean oc, boolean da) {
        checkArgs(t, r, m);
        title = t;
        rating = r;
        runtime = m;
        is3d = _3D;
        hasCC = cc;
        hasOC = oc;
        hasDA = da;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Rating getRating() {
        return rating;
    }

    @Override
    public int getRuntime() {
        return runtime;
    }

    @Override
    public boolean is3d() {
        return is3d;
    }

    @Override
    public boolean hasClosedCaptions() {
        return hasCC;
    }

    @Override
    public boolean hasOpenCaptions() {
        return hasOC;
    }

    @Override
    public boolean hasDescriptiveAudio() {
        return hasDA;
    }
    
    @Override
    public int hashCode() {
        int hash = 37 * title.hashCode();
        hash = 37 * hash + rating.toString().hashCode();
        hash = 37 * hash + (runtime << 4);
        hash += (is3d ? 8 : 0) & (hasCC ? 4 : 0) & (hasOC ? 2 : 0) & (hasDA ? 1 : 0); 
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Feature) {
            Feature ftr = (Feature) o;
            return this.getTitle().equals(ftr.getTitle()) && this.is3d() == ftr.is3d()
                    && this.hasClosedCaptions() == ftr.hasClosedCaptions()
                    && this.hasOpenCaptions() == ftr.hasOpenCaptions()
                    && this.hasDescriptiveAudio() == ftr.hasDescriptiveAudio()
                    && this.getRating().equals(ftr.getRating())
                    && this.getRuntime() == ftr.getRuntime();
        }
        return false;
    }

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
                hasDescriptiveAudio() ? "DA" : "").trim();
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
