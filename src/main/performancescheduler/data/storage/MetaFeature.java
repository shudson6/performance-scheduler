package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.Rating;

public class MetaFeature extends MetaWrapper<Feature> implements Feature {
    
    MetaFeature(Feature toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        super(toWrap, id, createTime, changeTime);
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
}
