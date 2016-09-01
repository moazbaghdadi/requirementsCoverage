package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.TestCase;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;

/**
 * SAX handler for test reports
 */
public class TestReportSAXHandler extends DefaultHandler {
    private Map<String, TestCase> testCases;
    private String testCaseName;

    public TestReportSAXHandler (PersistenceEntity persistenceEntity) {
        this.testCases= persistenceEntity.getTestCases();
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("testcase")){
            testCaseName = attributes.getValue("classname");
            testCases.put(testCaseName, new TestCase(testCaseName , true));
        } else if (qualifiedName.equals("failure")){
            testCases.get(testCaseName).setPositive(false);
        }
    }
}
