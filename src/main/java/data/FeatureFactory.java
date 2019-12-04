package main.java.data;

public class FeatureFactory {
    /**
     * Get a new instance. Must have a valid (ie non-null) title and rating as well as a 
     * positive runtime.
     * @param titile the name of the movie; not {@code null}
     * @param rating the movie's rating
     * @param runtime the runtime in minutes
     * @param id3d {@code true} if the feature is in 3D
     * @param hasCcap {@code true} if the feature has Closed Captions
     * @param hasOcap {@code true} if the feature has Open Captions
     * @param hasDescAudio {@code true} if the feature has Descriptive Audio
     * @return a new {@link Feature} instance
     */  
    public Feature createFeature(String title, Rating rating, int runtime,
            boolean is3d, boolean hasCcap, boolean hasOcap, boolean hasDescAudio) {
        
        return new FeatureImpl(title, rating, runtime, is3d, hasCcap, hasOcap, hasDescAudio);
    }
}
