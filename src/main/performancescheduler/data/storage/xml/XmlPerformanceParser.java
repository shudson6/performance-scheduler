package performancescheduler.data.storage.xml;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;
import performancescheduler.util.Context;

class XmlPerformanceParser {
    private PerformanceFactory pFactory;
    private int featureId;
    private int aud;
    private LocalDate date;
    private LocalTime time;
    private int trailer;
    private int cleanup;
    private int seating;
    private Performance perf;
    
    public XmlPerformanceParser() {
        pFactory = PerformanceFactory.newFactory();
        _clear();
    }
    
    public void clear() {
        _clear();
    }
    
    public int getFeatureId() {
        return featureId;
    }
    
    public Performance getPerformance() {
        return perf;
    }
    
    public boolean parse(XMLEventReader xmler) throws XMLStreamException {
        while (xmler.hasNext()) {
            XMLEvent event = xmler.nextEvent();
            if (event.isEndElement()
                    && event.asEndElement().getName().getLocalPart().equalsIgnoreCase(XML.PERFORMANCE)) {
                return createPerformance();
            } else if (event.isStartElement() && xmler.peek().isCharacters()) {
                parseChildEvent(event, xmler.nextEvent().asCharacters().getData());
            }
        }
        return false;
    }
    
    private void parseChildEvent(XMLEvent event, String data) throws XMLStreamException {
        switch (event.asStartElement().getName().getLocalPart()) {
            case XML.FEATURE_ID:
                featureId = parseFeatureID(data);
                break;
            case XML.AUDITORIUM:
                aud = parseIntData(data);
                break;
            case XML.DATE:
                date = parseDateData(data);
                break;
            case XML.TIME:
                time = parseTimeData(data);
                break;
            case XML.TRAILERS:
                trailer = parseIntData(data);
                break;
            case XML.CLEANUP:
                cleanup = parseIntData(data);
                break;
            case XML.SEATING:
                seating = parseIntData(data);
                break;
            case XML.PERFORMANCE_SCHEDULE:
            case XML.PERFORMANCE:
                break;
            default:
                System.err.println("Unknown event " + event.asStartElement().getName().getLocalPart()
                        + " within Performance data. Ignoring.");
                // every relevant case is covered.
        }
    }
    
    private LocalDate parseDateData(String data) throws XMLStreamException {
        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException ex) {
            throw new XMLStreamException("Could not parse date input in Performance data; see cause.", ex);
        }
    }
    
    private LocalTime parseTimeData(String data) throws XMLStreamException {
        try {
            return LocalTime.parse(data);
        } catch (DateTimeParseException ex) {
            throw new XMLStreamException("Could not parse time input in Performance data; see cause.", ex);
        }
    }
    
    private int parseFeatureID(String data) throws XMLStreamException {
        try {
            return Integer.parseUnsignedInt(data, XML.RADIX);
        } catch (NumberFormatException ex) {
            throw new XMLStreamException("NumberFormatException caught while parsing featureId: " + data, ex);
        }
    }
    
    private int parseIntData(String data) throws XMLStreamException {
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException ex) {
            throw new XMLStreamException("Failed to parse integer value from '" + data 
                    + "' within performance; see cause", ex);
        }
    }
    
    private boolean createPerformance() {
        if (date != null && time != null) {
            perf = pFactory.createPerformance(null, LocalDateTime.of(date, time), aud);
            return true;
        }
        return false;
    }
    
    private void _clear() {
        featureId = 0;
        aud = 0;
        date = null;
        time = null;
        trailer = 0;
        cleanup = 0;
        seating = 0;
        perf = null;
    }
}
