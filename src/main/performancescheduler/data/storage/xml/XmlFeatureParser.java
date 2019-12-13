package performancescheduler.data.storage.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import performancescheduler.data.Feature;
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Rating;

class XmlFeatureParser {
    private static final QName FEATURE_ID = new QName(XML.FEATURE_ID);
    
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
    
    public boolean parse(XMLEventReader xmler, XMLEvent event) throws XMLStreamException {
        if (event != null) {
            if (event.isStartElement() && event.asStartElement().getName().getLocalPart()
                    .equalsIgnoreCase(XML.FEATURE)) {
                extractFeatureID(event);
            }
        }
        while (xmler.hasNext()) {
            event = xmler.nextEvent();
            if (event.isEndElement()
                    && event.asEndElement().getName().getLocalPart().equalsIgnoreCase(XML.FEATURE)) {
                return createFeature();
            } else if (event.isStartElement() && xmler.peek().isCharacters()) {
                parseChildEvent(event, xmler.nextEvent().asCharacters().getData());
            }
        }
        return false;
    }

    private void parseChildEvent(XMLEvent event, String data) {
        switch (event.asStartElement().getName().getLocalPart()) {
            case XML.TITLE:
                title = data;
                break;
            case XML.RATING:
                setRating(data);
                break;
            case XML.RUNTIME:
                setRuntime(data);
                break;
            case XML.IS_3D:
                is3d = Boolean.parseBoolean(data);
                break;
            case XML.CCAP:
                cc = Boolean.parseBoolean(data);
                break;
            case XML.OCAP:
                oc = Boolean.parseBoolean(data);
                break;
            case XML.DESCRIPTIVE_AUDIO:
                da = Boolean.parseBoolean(data);
                break;
            case XML.PERFORMANCE_SCHEDULE:
            case XML.FEATURE:
                break;
            default:
                System.err.println("Unknown event " + event.asStartElement().getName().getLocalPart()
                        + " within Feature data. Ignoring.");
                // every relevant case is covered.
        }
    }
    
    private void setRuntime(String data) {
        try {
            runtime = Integer.parseInt(data);
        } catch (NumberFormatException ex) {
            System.err.println("Failed to parse Runtime value. Treating as 1.");
            runtime = 1;
        }
    }
    
    private void setRating(String data) {
        try {
            rating = Rating.valueOf(data);
        } catch (IllegalArgumentException ex) {
            System.err.println("Failed to parse Rating value. Treating as Rating.NR");
            rating = Rating.NR;
        }
    }

    private void extractFeatureID(XMLEvent event) throws XMLStreamException {
        try {
            id = Integer.parseInt(event.asStartElement().getAttributeByName(FEATURE_ID).getValue());
        } catch (NumberFormatException ex) {
            throw new XMLStreamException("Bad featureId attribute: see cause", ex);
        } catch (NullPointerException ex) {
            throw new XMLStreamException("No featureId attribute: see cause", ex);
        }
    }
    
    private boolean createFeature() {
        if (title != null) {
            ftr = ftrFactory.createFeature(title, rating, runtime, is3d, cc, oc, da);
            return true;
        }
        return false;
    }
    
    private void _clear() {
        ftr = null;
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
