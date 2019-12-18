package performancescheduler.data.storage;

import java.io.IOException;
import java.util.Collection;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

/**
 * Interface for classes that handle storage of Feature and Performance data.
 */
public interface Storage {
    /**
     * Load in the Features.
     * @return a new {@code Collection} of {@code Feature}s
     * @throws IOException in event of failure
     */
    public Collection<Feature> loadFeatureData() throws IOException;
    /**
     * Load in the Performances.
     * @return a new {@code Collection} of {@code Performance}s
     * @throws IOException in event of failure
     */
    public Collection<Performance> loadPerformanceData() throws IOException;
    /**
     * Save what you've been working on. Whether or not either parameter may be null is up to the implementation. 
     * @param featureData the features to save
     * @param performanceData the performances to save
     * @throws IOException in event of failure
     */
    public void save(Collection<Feature> featureData, Collection<Performance> performanceData) throws IOException;
}
