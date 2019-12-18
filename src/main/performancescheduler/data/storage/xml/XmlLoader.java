package performancescheduler.data.storage.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;

class XmlLoader {
    static final String NULL_MSG = "Parameters to XmlLoader.loadFile() must be non-null.";
    
    XMLEventReader xmler = null;
    Map<Integer, Feature> featureMap;
    Map<Performance, Integer> performanceMap;
    File file;
    XmlFeatureParser ftrParser;
    XmlPerformanceParser prfParser;
    PerformanceFactory pFactory;
    
    public XmlLoader(File toLoad) {
    	Objects.requireNonNull(toLoad);
    	file = toLoad;
        featureMap = new HashMap<>();
        performanceMap = new HashMap<>();
        pFactory = PerformanceFactory.newFactory();
        ftrParser = new XmlFeatureParser();
        prfParser = new XmlPerformanceParser();
    }
    
    public void load(Collection<Feature> features, Collection<Performance> performances)
    		throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        Objects.requireNonNull(features, NULL_MSG);
        Objects.requireNonNull(performances, NULL_MSG);
        try {
            xmler = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(file));
            parseXml();
            fixFeatures(features);
            fixPerformances(performances);
        } finally {
            if (xmler != null) {
                xmler.close();
            }
        }
    }
    
    private void fixFeatures(Collection<Feature> features) {
        features.addAll(featureMap.values());
    }
    
    private void fixPerformances(Collection<Performance> performances) {
        performanceMap.entrySet().stream().forEach(e -> {
            if (featureMap.containsKey(e.getValue())) {
                performances.add(pFactory.createPerformance(featureMap.get(e.getValue()), e.getKey().getDateTime(), 
                        e.getKey().getAuditorium()));
            } else {
                System.err.println("XmlLoader found no match for featureId=" + e.getValue() 
                        + " ; adding empty performance.");
                performances.add(e.getKey());
            }
        });
    }
    
    private void parseXml() throws XMLStreamException {
        while (xmler.hasNext()) {
            try {
                processEvent(xmler.nextEvent());
            } catch (XMLStreamException e) {
                System.err.println(e.getMessage());
                // if this is one of our exceptions thrown by Xml?*Parser
                // we want to recover and continue. otherwise, bail
                if (!(e.getCause() instanceof NumberFormatException)) {
                	throw e;
                }
            }
        }
    }
    
    private void processEvent(XMLEvent event) throws XMLStreamException {
        if (event.isStartElement()) {
            if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(XML.FEATURE)) {
                parseXmlFeature(event);
            } else if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(XML.PERFORMANCE)) {
                parseXmlPerformance();
            }
        }
    }
    
    private void parseXmlFeature(XMLEvent event) throws XMLStreamException {
        if (ftrParser.parse(xmler, event)) {
            featureMap.put(ftrParser.getFeatureID(), ftrParser.getFeature());
        }
    }
    
    private void parseXmlPerformance() throws XMLStreamException {
        prfParser.clear();
        if (prfParser.parse(xmler)) {
            performanceMap.put(prfParser.getPerformance(), prfParser.getFeatureId());
        }
    }
}
