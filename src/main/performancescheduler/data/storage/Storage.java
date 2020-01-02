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
     * Load in the data. Both parameters are required to be non-null; they will be cleared and populated with
     * objects loaded from storage.
     * @param featureData {@code Collection} to receive feature objects
     * @param performanceData {@code Collection} to receive performance objects
     * @throws IOException in event of failure
     */
    public void restore(Collection<Feature> featureData, Collection<Performance> performanceData) throws IOException;
    /**
     * Save what you've been working on. Whether or not either parameter may be null is up to the implementation. 
     * @param featureData the features to save
     * @param performanceData the performances to save
     * @throws IOException in event of failure
     */
    public void store(Collection<Feature> featureData, Collection<Performance> performanceData) throws IOException;
}
