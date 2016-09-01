package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Map;

/**
 * SAX handler for JacCoCo reports
 */
public class JaCoCoSAXHandler extends DefaultHandler {

    private String packageName;
    private File file;
    private TestCase testCase;
    private List<Line> lines;
    private Map<String, File> files;
    private final static Logger LOGGER = LoggerFactory.getLogger(JaCoCoSAXHandler.class);

    public JaCoCoSAXHandler (PersistenceEntity persistenceEntity, TestCase testCase) {
        this.lines = persistenceEntity.getLines();
        this.files = persistenceEntity.getFiles();
        this.testCase = testCase;
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("package")) {
            packageName = attributes.getValue("name");
        } else if (qualifiedName.equals("sourcefile")) {
            file = new File(packageName + "/" + attributes.getValue("name"));
            files.put(file.getFileName(), file);
        } else if (qualifiedName.equals("line") && attributes.getValue("mi").equals("0")) {
            Line line = new Line(Integer.valueOf(attributes.getValue("nr")), file.getFileName());
            line.addTestCase(testCase);
            lines.add(line);
        }
    }
}
