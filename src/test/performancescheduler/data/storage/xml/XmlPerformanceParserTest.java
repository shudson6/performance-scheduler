package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import performancescheduler.data.PerformanceFactory;
import performancescheduler.util.Context;

public class XmlPerformanceParserTest {
    static PerformanceFactory pFactory;
    
    XmlPerformanceParser parser;
    
    @BeforeClass
    public static void setUpBefore() {
        pFactory = PerformanceFactory.newFactory();
    }
    
    @Before
    public void setUp() {
        parser = new XmlPerformanceParser();
    }

    @Test
    public void testReadCorrectPerformanceData() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlPerformanceParserTest.class.getResourceAsStream("/xml/CorrectPerformance.xml"));
        assertTrue(parser.parse(xmler));
        assertEquals(Integer.parseInt("1234", XML.RADIX), parser.getFeatureId());
        assertEquals(pFactory.createPerformance(null, LocalDateTime.of(2019, 12, 30, 17, 45), Context.getAuditorium(5)),
                parser.getPerformance());
    }

    @Test
    public void testReadMissingDateTimeData() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlPerformanceParserTest.class.getResourceAsStream("/xml/MissingDateTime.xml"));
        assertFalse(parser.parse(xmler));
        parser.clear();
        assertFalse(parser.parse(xmler));
        parser.clear();
        assertFalse(parser.parse(xmler));
    }
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testReadBadIntegerInput() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlPerformanceParserTest.class.getResourceAsStream("/xml/BadIntPerformance.xml"));
        exception.expect(XMLStreamException.class);
        parser.parse(xmler);
    }
    
    @Test
    public void testReadBadDateInput() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlPerformanceParserTest.class.getResourceAsStream("/xml/BadDate.xml"));
        exception.expect(XMLStreamException.class);
        parser.parse(xmler);
    }
    
    @Test
    public void testReadBadTimeInput() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlPerformanceParserTest.class.getResourceAsStream("/xml/BadTime.xml"));
        exception.expect(XMLStreamException.class);
        parser.parse(xmler);
    }
    
    @Test
    public void testRunOutOfEvents() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlPerformanceParserTest.class.getResourceAsStream("/xml/empty.xml"));
        assertFalse(parser.parse(xmler));
    }
}
