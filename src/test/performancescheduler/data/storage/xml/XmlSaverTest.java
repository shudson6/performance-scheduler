package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import performancescheduler.data.Auditorium;
import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;
import performancescheduler.data.storage.MetaFeature;
import performancescheduler.data.storage.TestMetaFeature;
import performancescheduler.util.ChecksumVerifier;
import performancescheduler.util.UUIDGenerator;

public class XmlSaverTest {
    public static final String FTR_FILE = "singleFeature.xml";
    public static final String SIMPLE_FILE = "simpleSchedule.xml";
    public static final String TWINS_FILE = "noTwinFeature.xml";
    public static final String COLLISION_FILE = "hashCollisions.xml";
    public static final String NULPERF_FILE = "nullPerformance.xml";
    public static final String UNMAP_FILE = "unmappedFeature.xml";
    
    static FeatureFactory ftrFac = FeatureFactory.newFactory();
    static PerformanceFactory pFac = PerformanceFactory.newFactory();
    
    Collection<Feature> features;
    Collection<Performance> performances;
    
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        features = new ArrayList<>();
        performances = new ArrayList<>();
    }
    
    @Test
    public void writeEmptyPerformanceWhenFeatureNotMapped()
            throws IOException, XMLStreamException, FactoryConfigurationError {
        performances.add(pFac.createPerformance(new SpecialFeature("Foo", Rating.R, 137),
                LocalDateTime.of(2020, 1, 27, 19, 35), 1, 23, 7, 17));
        File toSave = temp.newFile(UNMAP_FILE);
        new XmlSaver(toSave).save(features, performances);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    @Test
    public void dontWriteNullPerformance() throws IOException, XMLStreamException, FactoryConfigurationError {
        performances.add(null);
        File toSave = temp.newFile(NULPERF_FILE);
        new XmlSaver(toSave).save(features, performances);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    @Test
    public void dontWriteTwinFeatures() throws XMLStreamException, FactoryConfigurationError, IOException {
        features.add(ftrFac.createFeature("Twin", Rating.G, 90, false, false, false, false));
        features.add(ftrFac.createFeature("Twin", Rating.G, 90, false, false, false, false));
        File toSave = temp.newFile(TWINS_FILE);
        new XmlSaver(toSave).save(features, null);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    @Test
    public void writeSingleFeatureTest() throws XMLStreamException, FactoryConfigurationError, IOException {
        features.add(ftrFac.createFeature("Foobar", Rating.NR, 110, false, true, false, false));
        File toSave = temp.newFile(FTR_FILE);
        new XmlSaver(toSave).save(features, null);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    @Test
    public void writeSingleFeatureAndPerformanceTest()
            throws IOException, XMLStreamException, FactoryConfigurationError {
        Feature f = ftrFac.createFeature("Foobar", Rating.NR, 110, false, true, false, false);
        features.add(f);
        performances.add(pFac.createPerformance(f, LocalDateTime.of(2020, 1, 27, 18, 15), 1, 23, 7, 17));
        File toSave = temp.newFile(SIMPLE_FILE);
        new XmlSaver(toSave).save(features, performances);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    @Test
    public void shouldThrowFileNotFound() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        exception.expect(FileNotFoundException.class);
        new XmlSaver(new File(System.getProperty("user.dir"))).save(features, null);
    }
    
    @Test
    public void shouldMapConsecutiveFeatureIDs() throws IOException, XMLStreamException, FactoryConfigurationError {
        features.add(new SpecialFeature("Foo", Rating.PG13, 111));
        features.add(new SpecialFeature("Bar", Rating.PG, 94));
        features.add(new SpecialFeature("Mel", Rating.NR, 131));
        File toSave = temp.newFile(COLLISION_FILE);
        new XmlSaver(toSave).save(features, null);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    // this class is here to test the mapFeatures method (private in XmlSaver)
    // by always reporting the same hashCode, it forces the method to generate unique mappings
    static class SpecialFeature extends TestMetaFeature {
        static final UUIDGenerator idGen = new UUIDGenerator();
        SpecialFeature(String title, Rating rating, int runtime) {
            super(ftrFac.createFeature(title, rating, runtime, false, true, false, true),
                    idGen.generateUUID(), LocalDateTime.now(), null);
        }
        @Override
        public int hashCode() {
            return 37;
        }
    }
}
