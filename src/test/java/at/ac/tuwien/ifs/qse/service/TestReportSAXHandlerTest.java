package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class TestReportSAXHandler.
 */
public class TestReportSAXHandlerTest {
    private TestReportSAXHandler handler;
    private PersistenceEntity persistenceEntity;

    @Before
    public void setUp() throws Exception {
        persistenceEntity = new PersistenceEntity(null, null, null);
        handler = new TestReportSAXHandler(persistenceEntity);
    }

    @Test
    public void testParsingReport() throws Exception {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(handler);

        parser.parse("src/test/resources/Test-org.testReport.xml");
        assertEquals(4, persistenceEntity.getTestCases().size());
        assertEquals(2, persistenceEntity.getTestCases().stream().
                filter(at.ac.tuwien.ifs.qse.model.TestCase::isPositive)
                .count());
    }
}