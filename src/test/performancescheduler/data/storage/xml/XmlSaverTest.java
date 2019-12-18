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
import performancescheduler.util.ChecksumVerifier;

public class XmlSaverTest {
    public static final String FTR_FILE = "singleFeature.xml";
    public static final String SIMPLE_FILE = "simpleSchedule.xml";
    
    FeatureFactory ftrFac = FeatureFactory.newFactory();
    PerformanceFactory pFac = PerformanceFactory.newFactory();
    
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
        performances.add(pFac.createPerformance(f, LocalDateTime.of(2020, 1, 27, 18, 15),
                Auditorium.getInstance(1, null, true, 100)));
        File toSave = temp.newFile(SIMPLE_FILE);
        new XmlSaver(toSave).save(features, performances);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(toSave));
    }
    
    @Test
    public void shouldThrowFileNotFound() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        exception.expect(FileNotFoundException.class);
        new XmlSaver(new File(System.getProperty("user.dir"))).save(features, null);
    }
}
