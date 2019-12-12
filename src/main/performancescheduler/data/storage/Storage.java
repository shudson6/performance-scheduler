package performancescheduler.data.storage;

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
     */
    public Collection<Feature> loadFeatureData();
    /**
     * Load in the Performances.
     * @return a new {@code Collection} of {@code Performance}s
     */
    public Collection<Performance> loadPerformanceData();
    /**
     * Save what you've been working on. Whether or not either parameter may be null is up to the implementation. 
     * @param featureData the features to save
     * @param performanceData the performances to save
     */
    public void save(Collection<Feature> featureData, Collection<Performance> performanceData);
}
