package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;

public class XmlFeatureParserTest {
    static FeatureFactory ftrFactory;
    
    XmlFeatureParser parser;
    
    @BeforeClass
    public static void setUpBefore() {
        ftrFactory = FeatureFactory.newFactory();
    }
    
    @Before
    public void setUp() {
        parser = new XmlFeatureParser();
    }

    @Test
    public void readCorrectFeature() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/xml/CorrectFeature.xml"));
        assertTrue(parser.parse(xmler));
        assertEquals(1234, parser.getFeatureID());
        assertEquals(ftrFactory.createFeature("Foobar", Rating.PG13, 119, false, true, false, true),
                parser.getFeature());
        xmler.close();
    }
    
    @Test
    public void testClear() {
        parser.clear();
        assertEquals(null, parser.getFeature());
    }
    
    @Test
    public void testRecoverableExceptionsInFile() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/xml/RecoverableFeatures.xml"));
        assertTrue(parser.parse(xmler));
        assertEquals("Pew Pew", parser.getFeature().getTitle());
        assertTrue(parser.parse(xmler));
        assertEquals("Bad Input", parser.getFeature().getTitle());
        assertEquals(1, parser.getFeature().getRuntime());
        assertEquals(Rating.NR, parser.getFeature().getRating());
        // next to be read is the one with no title
        parser.clear();
        assertFalse(parser.parse(xmler));
    }
    
    @Test
    public void testNoTitleToCreate() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/xml/empty.xml"));
        assertFalse(parser.parse(xmler));
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void extractBadFeatureId() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/xml/badFeatureId.xml"));
        exception.expect(XMLStreamException.class);
        parser.parse(xmler);
    }
    
    @Test
    public void extractNoFeatureId() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/xml/noFeatureId.xml"));
        exception.expect(XMLStreamException.class);
        parser.parse(xmler);
    }
}
