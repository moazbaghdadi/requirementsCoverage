package at.ac.tuwien.ifs.qse.xmlParser;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.List;

/**
 * Runs an XML Parser
 */
public class ParserRunner {

    public static void runXMLParser (ContentHandler handler, List<String> pathsToXMLFiles) throws SAXException, IOException {
        for (String pathToXMLFile : pathsToXMLFiles) {
            runXMLParser(handler, pathToXMLFile);
        }
    }

    public static void runXMLParser (ContentHandler handler, String pathToXMLFile) throws SAXException, IOException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(handler);
        parser.setFeature("http://xml.org/sax/features/validation", false);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        parser.parse(pathToXMLFile);
    }
}
