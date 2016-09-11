package at.ac.tuwien.ifs.qse.xmlParser;

import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for JacCoCo reports
 */
public class JaCoCoSAXHandler extends DefaultHandler {

    private String packageName;
    private String file;
    private TestCase testCase;
    private Persistence persistence;

    public JaCoCoSAXHandler (Persistence persistence, TestCase testCase) {
        this.persistence = persistence;
        this.testCase = testCase;
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        switch (qualifiedName) {
            case "package":
                packageName = attributes.getValue("name");
                break;
            case "sourcefile":
                file = packageName.replace("/", ".") + "." + attributes.getValue("name");
                break;
            case "line":
                if (attributes.getValue("mi").equals("0")) {
                    Line line = persistence.getRelevantLine(Integer.valueOf(attributes.getValue("nr")), file);
                    if (line != null) {
                        line.addTestCase(testCase);
                        persistence.addRelevantLine(line);
                    }
                }
                break;
        }
    }
}
