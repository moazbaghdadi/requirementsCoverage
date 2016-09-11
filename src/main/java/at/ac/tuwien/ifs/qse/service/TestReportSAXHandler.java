package at.ac.tuwien.ifs.qse.service;

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

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("testcase")){
            String testCaseName = attributes.getValue("classname");
            testCase = persistence.getTestCase(testCaseName);
            if (testCase == null) {
                testCase = new TestCase(testCaseName, true);
                persistence.addTestCase(testCase);
            }
        } else if (qualifiedName.equals("failure")){
            testCase.setPositive(false);
            persistence.addTestCase(testCase);
        }
    }
}
