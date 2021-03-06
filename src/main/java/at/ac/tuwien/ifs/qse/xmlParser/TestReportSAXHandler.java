package at.ac.tuwien.ifs.qse.xmlParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for test reports
 */
public class TestReportSAXHandler extends DefaultHandler {
    private TestCase testCase;
    private Persistence persistence;

    public TestReportSAXHandler (Persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("testcase")){
            String testCaseName = attributes.getValue("classname") + "#" + attributes.getValue("name");
            testCase = new TestCase(testCaseName, true);
        } else if (qualifiedName.equals("failure")){
            testCase.setPositive(false);
        }
    }

    @Override
    public void endElement (String namespaceURI,
                            String localName,
                            String qualifiedName) throws SAXException {
        if (qualifiedName.equals("testcase")){
            persistence.addTestCase(testCase);
        }
    }
}
