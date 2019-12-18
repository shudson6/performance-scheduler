package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
    
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    
    @Before
    public void setUp() {
        features = buildFeatureList();
        performances = buildPerformanceList();
    }
    
    @Test
    public void shouldSaveFeaturesWhenPerformancesNull() throws IOException {
        File file = temp.newFile("nullPerformances.xml");
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
