package main.data;

/**
 * Interface to provide basic necessary information about a feature.
 * 
 * @author Steven Hudson
 */
public interface Feature {
    /**
     * @return the title of this feature
     */
    public String getTitle();
    /**
     * @return the rating of this feature
     */
    public Rating getRating();
    /**
     * @return the runtime of this feature, in minutes
     */
    public int getRuntime();
    /**
     * @return {@code true} if this feature is 3D
     */
    public boolean is3D();
    /**
     * @return {@code true} if this feature has Closed Caption data
     */
    public boolean hasClosedCaptions();
    /**
     * @return {@code true} if this feature has Open Caption data
     */
    public boolean hasOpenCaptions();
    /**
     * @return {@code true} if this feature has Descriptive Audio data
     */
    public boolean hasDescriptiveAudio();
}
