package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class XmlFeatureParserTest {

    @Test
    public void test() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/CorrectFeature.xml"));
        XmlFeatureParser parser = new XmlFeatureParser();
        assertTrue(parser.parse(xmler));
        assertEquals(1234, parser.getFeatureID());
    }

}
