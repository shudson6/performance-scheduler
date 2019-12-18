package performancescheduler.data.storage.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

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

    @Override
    public Collection<Feature> loadFeatureData() throws IOException {
        if (features == null) {
            load();
        }
    }

    @Override
    public Collection<Performance> loadPerformanceData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(Collection<Feature> featureData, Collection<Performance> performanceData) {
        // TODO Auto-generated method stub
        
    }

    private void load() throws IOException {
        try {
            loader.load(features, performances);
        } catch (FileNotFoundException e) {
            rethrow(e, "Could not find the file " + file.getAbsolutePath());
        } catch (XMLStreamException e) {
            rethrow(e, "Error while parsing " + file.getAbsolutePath());
        } catch (FactoryConfigurationError e) {
            rethrow(e, "FactoryConfigurationError thrown by XmlLoader");
        }
    }
    
    private void rethrow(Throwable cause, String message) throws IOException {
        throw new IOException(message, cause);
    }
}
