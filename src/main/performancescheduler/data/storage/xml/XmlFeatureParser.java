package performancescheduler.data.storage.xml;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;

public class XmlFeatureParser {
    private FeatureFactory ftrFactory;
    private Feature ftr;    
    private int id;
    private String title;
    private Rating rating;
    private int runtime;
    private boolean is3d;
    private boolean cc;
    private boolean oc;
    private boolean da;
    
    public XmlFeatureParser() {
        ftrFactory = FeatureFactory.newFactory();
        _clear();
    }
    
    public void clear() {
        _clear();
    }
    
    public Feature getFeature() {
        return ftr;
    }
    
    public int getFeatureID() {
        return id;
    }
    
    public boolean parse(XMLEventReader xmler) throws XMLStreamException {
        while (xmler.hasNext()) {
            XMLEvent event = xmler.nextEvent();
            if (event.isEndElement()
                    && event.asEndElement().getName().getLocalPart().equalsIgnoreCase(XML.FEATURE)) {
                return createFeature();
            }
        }
        return false;
    }
    
    private boolean createFeature() {
        if (title != null) {
            ftr = ftrFactory.createFeature(title, rating, runtime, is3d, cc, oc, da);
            return true;
        }
        return false;
    }
    
    private void _clear() {
        id = 0;
        title = null;
        rating = Rating.NR;
        runtime = 1;
        is3d = false;
        cc = false;
        oc = false;
        da = false;
    }
}
