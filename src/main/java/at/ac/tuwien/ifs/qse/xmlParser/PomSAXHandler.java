package at.ac.tuwien.ifs.qse.xmlParser;

import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses pom.xml files to extract the target project name
 */
public class PomSAXHandler extends DefaultHandler {

    private Persistence persistence;
    private int tagLevel = 0;
    private boolean storeProjectName;

    public PomSAXHandler(Persistence persistence) {
        this.persistence = persistence;
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        tagLevel++;
        if("name".equals(qualifiedName) && tagLevel == 2) {
            storeProjectName = true;
        }
    }

    public void characters(char[] text, int start, int length)
            throws SAXException {
        if (storeProjectName){
            persistence.setProjectName(new String(text, start, length));
            storeProjectName = false;
        }
    }

    public void endElement (String namespaceURI,
                            String localName,
                            String qualifiedName) throws SAXException {
        tagLevel--;

    }
}
