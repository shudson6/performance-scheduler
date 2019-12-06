package performancescheduler.data;

public class FeatureWrapper extends Wrapper<Feature> implements Feature {
    
    protected FeatureWrapper(Feature toWrap) {
        super(toWrap);
    }

    @Override
    public int compareTo(Feature o) {
        return wrapped.compareTo(o);
    }

    @Override
    public String getTitle() {
        return wrapped.getTitle();
    }

    @Override
    public Rating getRating() {
        return wrapped.getRating();
    }

    @Override
    public int getRuntime() {
        return wrapped.getRuntime();
    }

    @Override
    public boolean is3d() {
        return wrapped.is3d();
    }

    @Override
    public boolean hasClosedCaptions() {
        return wrapped.hasClosedCaptions();
    }

    @Override
    public boolean hasOpenCaptions() {
        return wrapped.hasOpenCaptions();
    }

    @Override
    public boolean hasDescriptiveAudio() {
        return wrapped.hasDescriptiveAudio();
    }
    
    @Override
    public String toString() {
        return wrapped.toString();
    }
}
