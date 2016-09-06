package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * SAX handler for JacCoCo reports
 */
public class JaCoCoSAXHandler extends DefaultHandler {

    private String packageName;
    private String file;
    private TestCase testCase;
    private List<Line> lines;

    public JaCoCoSAXHandler (PersistenceEntity persistenceEntity, TestCase testCase) {
        this.lines = persistenceEntity.getLines();
        this.testCase = testCase;
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("package")) {
            packageName = attributes.getValue("name");
        } else if (qualifiedName.equals("sourcefile")) {
            file = packageName.replace("/", ".") + "." + attributes.getValue("name");
        } else if (qualifiedName.equals("line")) {
            Line coveredLine = new Line(Integer.valueOf(attributes.getValue("nr")), file);
            for (Line line :
                    lines) {
                if (line.equals(coveredLine)) {
                    if(attributes.getValue("mi").equals("0")) {
                        line.addTestCase(testCase);
                    }
                    line.setRelevant(true);
                    break;
                }
            }
        }
    }
}
