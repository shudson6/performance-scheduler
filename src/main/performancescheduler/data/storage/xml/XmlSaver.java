package performancescheduler.data.storage.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Objects;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import performancescheduler.data.Feature;
import performancescheduler.data.Performance;

class XmlSaver {
	private final File file;
	XMLEventWriter xmlew;
	
	public XmlSaver(File toSave) {
		Objects.requireNonNull(toSave);
		file = toSave;
	}
	
	public void save(Collection<Feature> features, Collection<Performance> performances)
			throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
		Objects.requireNonNull(features);
		xmlew = XMLOutputFactory.newFactory().createXMLEventWriter(new FileOutputStream(file));
		xmlew.close();
	}
}
