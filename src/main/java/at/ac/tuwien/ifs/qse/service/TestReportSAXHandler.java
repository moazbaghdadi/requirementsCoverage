package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;

/**
 * SAX handler for test reports
 */
public class TestReportSAXHandler extends DefaultHandler {
    private Map<String, TestCase> testCases = ModelAccessService.getTestCases();
    private String testCaseName;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestReportSAXHandler.class);

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
