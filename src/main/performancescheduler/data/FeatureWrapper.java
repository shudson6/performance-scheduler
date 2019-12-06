package performancescheduler.data;

public class FeatureWrapper implements Feature {
    private Feature feature;
    
    protected FeatureWrapper(Feature ftr) {
        setWrappedFeature(ftr);
    }
    
    public final void setWrappedFeature(Feature ftr) {
        if (ftr == null) {
            throw new NullPointerException("FeatureWrapper: feature must not be null.");
        }
        // do not allow wrapping this; infinite recursion would ensue!
        preventRecursiveWrap(ftr);
        feature = ftr;
    }
    
    @Override
    public boolean equals(Object o) {
        return feature.equals(o);
    }

    @Override
    public int compareTo(Feature o) {
        return feature.compareTo(o);
    }

    @Override
    public String getTitle() {
        return feature.getTitle();
    }

    @Override
    public Rating getRating() {
        return feature.getRating();
    }

    @Override
    public int getRuntime() {
        return feature.getRuntime();
    }

    @Override
    public boolean is3d() {
        return feature.is3d();
    }

    @Override
    public boolean hasClosedCaptions() {
        return feature.hasClosedCaptions();
    }

    @Override
    public boolean hasOpenCaptions() {
        return feature.hasOpenCaptions();
    }

    @Override
    public boolean hasDescriptiveAudio() {
        return feature.hasDescriptiveAudio();
    }
    
    @Override
    public String toString() {
        return feature.toString();
    }
    
    @Override
    public int hashCode() {
        return feature.hashCode();
    }

    private void preventRecursiveWrap(Feature ftr) {
        if (ftr instanceof FeatureWrapper) {
            if (ftr == this) {
                throw new IllegalArgumentException("FeatureWrapper may not wrap itself.");
            } else {
                preventRecursiveWrap(((FeatureWrapper) ftr).feature);
            }
        }
    }
}
