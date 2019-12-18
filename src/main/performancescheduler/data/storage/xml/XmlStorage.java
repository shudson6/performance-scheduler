package performancescheduler.data.storage.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.storage.Storage;

public class XmlStorage implements Storage {
    private File file;
    private XmlLoader loader;
    private Collection<Feature> features = null;
    private Collection<Performance> performances = null;
    
    public static XmlStorage getInstance(File file) {
        Objects.requireNonNull(file);
        return new XmlStorage(file);
    }
    
    public static XmlStorage getInstance(String filename) {
        Objects.requireNonNull(filename);
        return getInstance(new File(filename));
    }

    @Override
    public Collection<Feature> restoreFeatureData() throws IOException {
        if (features == null) {
            load();
        }
        return features;
    }

    @Override
    public Collection<Performance> restorePerformanceData() throws IOException {
        if (performances == null) {
            load();
        }
        return performances;
    }

    @Override
    public void store(Collection<Feature> featureData, Collection<Performance> performanceData) throws IOException {
        // clear these; they are no longer valid but it's also not certain that the input data is valid
        features = null;
        performances = null;
        
        try {
            new XmlSaver(file).save(featureData, performanceData);
        } catch (FileNotFoundException e) {
            rethrow(e, "Specified file " + file.getAbsolutePath() + " could not be written to.");
        } catch (XMLStreamException | FactoryConfigurationError e) {
            rethrow(e, "Error while storing " + file.getAbsolutePath());
        } 
    }

    private void load() throws IOException {
        try {
            loader.load(features, performances);
        } catch (FileNotFoundException e) {
            rethrow(e, "Could not find the file " + file.getAbsolutePath());
        } catch (XMLStreamException | FactoryConfigurationError e) {
            rethrow(e, "Error while parsing " + file.getAbsolutePath());
        } 
    }
    
    private void rethrow(Throwable cause, String message) throws IOException {
        throw new IOException(message, cause);
    }
    
    private XmlStorage(File f) {
        file = f;
        loader = new XmlLoader(file);
    }
}
