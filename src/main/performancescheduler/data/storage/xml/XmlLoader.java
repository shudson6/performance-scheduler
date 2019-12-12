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
import performancescheduler.data.FeatureFactory;
import performancescheduler.data.Performance;
import performancescheduler.data.PerformanceFactory;

class XmlLoader {
    static final String NULL_MSG = "Parameters to XmlLoader.loadFile() must be non-null.";
    
    XMLEventReader xmler = null;
    Map<Integer, Feature> featureMap;
    Map<Performance, Integer> performanceMap;
    
    XmlFeatureParser ftrParser;
    PerformanceFactory pFactory;
    
    public XmlLoader(File file) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        xmler = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(file));
        featureMap = new HashMap<>();
        performanceMap = new HashMap<>();
        pFactory = PerformanceFactory.newFactory();
        ftrParser = new XmlFeatureParser();
    }
    
    public void load(Collection<Feature> features, Collection<Performance> performances) {
        Objects.requireNonNull(features, NULL_MSG);
        Objects.requireNonNull(performances, NULL_MSG);
        parseXml();
        fixFeatures(features);
        fixPerformances(performances);
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
                System.err.println("XmlLoader found no match for specified feature; adding empty performance.");
                performances.add(e.getKey());
            }
        });
    }
    
    private void parseXml() {
        while (xmler.hasNext()) {
            try {
                processEvent(xmler.peek());
            } catch (XMLStreamException e) {
                // print the message but keep reading events
                System.err.println(e.getMessage());
            }
        }
    }
    
    private void processEvent(XMLEvent event) throws XMLStreamException {
        if (event.isStartElement()) {
            if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(XML.FEATURE)) {
                parseXmlFeature();
            } else if (event.asStartElement().getName().getLocalPart().equalsIgnoreCase(XML.PERFORMANCE)) {
                parseXmlPerformance();
            }
        }
    }
    
    private void parseXmlFeature() throws XMLStreamException {
        if (ftrParser.parse(xmler)) {
            featureMap.put(ftrParser.getFeatureID(), ftrParser.getFeature());
        }
    }
    
    private void parseXmlPerformance() throws XMLStreamException {
        
    }
}