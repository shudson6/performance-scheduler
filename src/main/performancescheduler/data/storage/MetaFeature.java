package performancescheduler.data.storage;

import java.time.LocalDateTime;
import java.util.UUID;

import performancescheduler.data.Feature;
import performancescheduler.data.Rating;

public class MetaFeature extends MetaWrapper<Feature> implements Feature {
    
    protected MetaFeature(Feature toWrap, UUID id, LocalDateTime createTime, LocalDateTime changeTime) {
        super(toWrap, id, createTime, changeTime);
    }

    @Override
    public int compareTo(Feature o) {
    	if (wrapped != null) {
    		return wrapped.compareTo(o);
    	}
    	// wrapped = null
    	if (o instanceof MetaFeature) {
    		return (((MetaFeature) o).wrapped == null) ? 0 : 1;
    	} else {
    		return (o == null) ? 0 : 1;
    	}
    }

    @Override
    public String getTitle() {
        return (wrapped == null) ? NULLSTR : wrapped.getTitle();
    }

    @Override
    public Rating getRating() {
        return (wrapped != null) ? wrapped.getRating() : null;
    }

    @Override
    public int getRuntime() {
        return (wrapped != null) ? wrapped.getRuntime() : 0;
    }

    @Override
    public boolean is3d() {
        return (wrapped != null) ? wrapped.is3d() : false;
    }

    @Override
    public boolean hasClosedCaptions() {
        return (wrapped != null) ? wrapped.hasClosedCaptions() : false;
    }

    @Override
    public boolean hasOpenCaptions() {
        return (wrapped != null) ? wrapped.hasOpenCaptions() : false;
    }

    @Override
    public boolean hasDescriptiveAudio() {
        return (wrapped != null) ? wrapped.hasDescriptiveAudio() : false;
    }
}
