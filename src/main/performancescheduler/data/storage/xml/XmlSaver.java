package performancescheduler.data.storage.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

class XmlSaver {
	private final File file;
	XMLEventFactory xmlFactory = XMLEventFactory.newFactory();
	XMLStreamWriter xmlw = null;
	
	public XmlSaver(File toSave) {
		Objects.requireNonNull(toSave);
		file = toSave;
	}
	
	public void save(Collection<Feature> features, Collection<Performance> performances)
			throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
		Objects.requireNonNull(features);
		try {
		    xmlw = XMLOutputFactory.newFactory().createXMLStreamWriter(new FileOutputStream(file));
		    write(features, performances);
		} finally {
		    if (xmlw != null) {
		        xmlw.close();
		    }
		}
	}
	
	private void write(Collection<Feature> features, Collection<Performance> performances) throws XMLStreamException {
	    Map<Feature, Integer> featureMap = mapFeatures(features);
	    xmlw.writeStartDocument();
	    xmlw.writeStartElement(XML.PERFORMANCE_SCHEDULE);
	    writeFeatures(featureMap);
	    writePerformances(featureMap, performances);
	    xmlw.writeEndDocument();
	    xmlw.flush();
	}
	
	private void writePerformances(Map<Feature, Integer> featureMap, Collection<Performance> performances)
	        throws XMLStreamException {
	    // this method is (obviously) always called internally, featureMap will always contain (null, 0) entry
	    // let's just make sure performances has something to work with
	    if (performances == null) {
	        return;
	    }
        for (Performance p : performances) {
            if (p != null) {
                if (featureMap.containsKey(p.getFeature())) {
                    writePerformance(p, featureMap.get(p.getFeature()));
                } else {
                    writePerformance(p, featureMap.get(null));
                }
            }
        }
    }

    private void writePerformance(Performance p, Integer i) throws XMLStreamException {
        xmlw.writeStartElement(XML.PERFORMANCE);
        writeSimpleElement(XML.FEATURE_ID, Integer.toUnsignedString(i, XML.RADIX));
        writeSimpleElement(XML.AUDITORIUM, Integer.toString(p.getAuditorium()));
        writeSimpleElement(XML.DATE, p.getDate().toString());
        writeSimpleElement(XML.TIME, p.getTime().toString());
        writeSimpleElement(XML.TRAILERS, Integer.toString(p.getTrailers()));
        writeSimpleElement(XML.CLEANUP, Integer.toString(p.getCleanup()));
        writeSimpleElement(XML.SEATING, Integer.toString(p.getSeating()));
        xmlw.writeEndElement();
    }

    private void writeFeatures(Map<Feature, Integer> featureMap) throws XMLStreamException {
        for (Map.Entry<Feature, Integer> e : featureMap.entrySet()) {
            writeFeature(e.getKey(), e.getValue());
        }
    }

    private void writeFeature(Feature f, Integer i) throws XMLStreamException {
        if (f != null) {
            xmlw.writeStartElement(XML.FEATURE);
            xmlw.writeAttribute(XML.FEATURE_ID, Integer.toUnsignedString(i, XML.RADIX));
            writeSimpleElement(XML.TITLE, f.getTitle());
            writeSimpleElement(XML.RATING, f.getRating().toString());
            writeSimpleElement(XML.RUNTIME, Integer.toString(f.getRuntime()));
            writeSimpleElement(XML.IS_3D, Boolean.toString(f.is3d()));
            writeSimpleElement(XML.CCAP, Boolean.toString(f.hasClosedCaptions()));
            writeSimpleElement(XML.OCAP, Boolean.toString(f.hasOpenCaptions()));
            writeSimpleElement(XML.DESCRIPTIVE_AUDIO, Boolean.toString(f.hasDescriptiveAudio()));
            xmlw.writeEndElement();
        }
    }
    
    private void writeSimpleElement(String name, String characters) throws XMLStreamException {
        xmlw.writeStartElement(name);
        xmlw.writeCharacters(characters);
        xmlw.writeEndElement();
    }

    private Map<Feature, Integer> mapFeatures(Collection<Feature> features) {
	    // setting initial capacity to double the size of the collection to decrease the probability of
	    // collisions and prevent rehash operations
	    Map<Feature, Integer> featureMap = new HashMap<>(features.size() * 2);
	    // always put a "no feature" option first
	    featureMap.put(null, 0);
	    for (Feature f : features) {
	        if (featureMap.containsKey(f)) {
	            continue;
	        }
	        // in the event of a hash collision, we need to guarantee the uniqueness of the featureId in the output
	        // so, we store the hash code here and will adjust it if it is already in the map
	        int code = f.hashCode();
	        while (featureMap.containsValue(code)) {
	            code++;
	        }
	        featureMap.put(f, code);
	    }
	    return featureMap;
	}
}
