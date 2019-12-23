package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

public class XmlStorageTest {
    static Auditorium aud1 = Auditorium.getInstance(1, null, false, 80);
    static Auditorium aud2 = Auditorium.getInstance(2, null, true, 50);
    static FeatureFactory fFac = FeatureFactory.newFactory();
    static PerformanceFactory pFac = PerformanceFactory.newFactory();
    
    List<Feature> features;
    List<Performance> performances;
    File file;
    
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Before
    public void setUp() {
        features = buildFeatureList();
        performances = buildPerformanceList();
    }
    
    @Test
    public void badXmlShouldThrowIOException() throws IOException {
        exception.expect(IOException.class);
        XmlStorage.getInstance(XmlStorageTest.class.getResource("/xml/NotWellFormed.xml").getFile())
                .restore(new ArrayList<Feature>(), null);
    }
    
    @Test
    public void fileNotFoundOnRestore() throws IOException {
        file = new File(System.getProperty("user.dir"));
        exception.expect(IOException.class);
        XmlStorage.getInstance(file).restore(new ArrayList<Feature>(), null);
    }
    
    @Test
    public void fileNotFoundOnStore() throws IOException {
        file = new File(System.getProperty("user.dir"));
        exception.expect(IOException.class);
        XmlStorage.getInstance(file).store(features, performances);
    }
    
    @Test
    public void testLoadFeaturesAndPerformances() throws IOException {
        String fname = XmlStorageTest.class.getResource("/xml/Normal.xml").getFile();
        // verify checksum of the file so we know it hasn't changed
        assertTrue(ChecksumVerifier.getInstance().verifyFile(new File(fname)));
        XmlStorage xml = XmlStorage.getInstance(fname);
        ArrayList<Feature> ftrl = new ArrayList<>();
        ArrayList<Performance> pfml = new ArrayList<>();
        xml.restore(ftrl, pfml);
        assertTrue(features.containsAll(ftrl));
        assertTrue(ftrl.containsAll(features));
        assertTrue(performances.containsAll(pfml));
        assertTrue(pfml.containsAll(performances));
    }
    
    // same test as above, but reversed order of Performances and Features
    @Test
    public void testLoadPerformancesAndFeatures() throws IOException {
        String fname = XmlStorageTest.class.getResource("/xml/Normal.xml").getFile();
        // verify checksum of the file so we know it hasn't changed
        assertTrue(ChecksumVerifier.getInstance().verifyFile(new File(fname)));
        XmlStorage xml = XmlStorage.getInstance(fname);
        ArrayList<Feature> ftrl = new ArrayList<>();
        ArrayList<Performance> pfml = new ArrayList<>();
        xml.restore(ftrl, pfml);
        assertTrue(performances.containsAll(pfml));
        assertTrue(pfml.containsAll(performances));
        assertTrue(features.containsAll(ftrl));
        assertTrue(ftrl.containsAll(features));
    }
    
    @Test
    public void shouldSaveFeaturesAndPerformances() throws IOException {
        file = temp.newFile("expectedCaseSave.xml");
        XmlStorage.getInstance(file).store(features, performances);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(file));
    }
    
    @Test
    public void shouldSaveFeaturesWhenPerformancesNull() throws IOException {
        file = temp.newFile("nullPerformances.xml");
        XmlStorage.getInstance(file).store(features, null);
        assertTrue(ChecksumVerifier.getInstance().verifyFile(file));
    }
    
    private List<Feature> buildFeatureList() {
        List<Feature> f = new ArrayList<>();
        f.add(fFac.createFeature("Foo", Rating.R, 90, false, true, false, false));
        f.add(fFac.createFeature("Bar", Rating.NC17, 110, false, true, false, false));
        return f;
    }
    
    private List<Performance> buildPerformanceList() {
        List<Performance> p = new ArrayList<>();
        p.add(pFac.createPerformance(features.get(0), LocalDateTime.of(2020, 2, 13, 13, 37), aud1));
        p.add(pFac.createPerformance(features.get(0), LocalDateTime.of(2020, 2, 13, 17, 54), aud1));
        p.add(pFac.createPerformance(features.get(1), LocalDateTime.of(2020, 2, 13, 15, 45), aud2));
        return p;
    }
}
