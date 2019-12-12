package performancescheduler.data.storage.xml;

import static org.junit.Assert.*;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.BeforeClass;
import org.junit.Test;

import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;

public class XmlFeatureParserTest {
    static FeatureFactory ftrFactory;
    
    @BeforeClass
    public static void setUpBefore() {
        ftrFactory = FeatureFactory.newFactory();
    }

    @Test
    public void readCorrectFeature() throws XMLStreamException, FactoryConfigurationError {
        XMLEventReader xmler = XMLInputFactory.newFactory().createXMLEventReader(
                XmlFeatureParserTest.class.getResourceAsStream("/xml/CorrectFeature.xml"));
        XmlFeatureParser parser = new XmlFeatureParser();
        assertTrue(parser.parse(xmler));
        assertEquals(1234, parser.getFeatureID());
        assertEquals(ftrFactory.createFeature("Foobar", Rating.PG13, 119, false, true, false, true),
                parser.getFeature());
        xmler.close();
    }

}
