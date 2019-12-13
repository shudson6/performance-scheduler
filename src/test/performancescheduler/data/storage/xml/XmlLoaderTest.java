package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.data.Rating;
import performancescheduler.util.Context;

public class XmlLoaderTest {
	static final FeatureFactory ftrFactory = FeatureFactory.newFactory();
	static final PerformanceFactory prfFactory = PerformanceFactory.newFactory();

	List<Feature> features = new ArrayList<>();
	List<Performance> performances = new ArrayList<>();
	
	@Before
	public void setUp() {
		features = new ArrayList<>();
		performances = new ArrayList<>();
	}

	@Test
	public void test() throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
		Feature ftr1234 = ftrFactory.createFeature("Foobar", Rating.PG13, 95, false, false, false, false);
		new XmlLoader(new File(XmlLoaderTest.class.getResource("/xml/LoaderTest.xml").getFile()))
				.load(features, performances);
		assertTrue(features.contains(ftr1234));
		assertTrue(performances.contains(prfFactory.createPerformance(ftr1234, LocalDateTime.of(2019, 12, 12, 8, 30),
				Context.getAuditorium(5))));
		assertTrue(performances.contains(prfFactory.createPerformance(null, LocalDateTime.of(2019, 12, 13, 9, 0),
				Context.getAuditorium(5))));
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void badXmlShouldCauseXmlStreamException() 
			throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
		exception.expect(XMLStreamException.class);
		new XmlLoader(new File(XmlLoaderTest.class.getResource("/xml/BadAttribute.xml").getFile()))
				.load(features, performances);
	}
}
