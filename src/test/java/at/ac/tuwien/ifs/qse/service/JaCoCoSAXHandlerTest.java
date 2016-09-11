package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import at.ac.tuwien.ifs.qse.xmlParser.JaCoCoSAXHandler;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class JaCoCoSAXHandler.
 */
public class JaCoCoSAXHandlerTest {
    private PersistenceEntity persistenceEntity;
    private JaCoCoSAXHandler handler;

    @Before
    public void setUp() throws Exception {
        persistenceEntity = new PersistenceEntity(null, null, null);
        at.ac.tuwien.ifs.qse.model.TestCase testCase = new at.ac.tuwien.ifs.qse.model.TestCase("test1.java", true);
        persistenceEntity.getTestCases().add(testCase);

        handler = new JaCoCoSAXHandler(persistenceEntity, testCase);

        persistenceEntity.addLine(new Line(1, "package.source1.java"));
        persistenceEntity.addLine(new Line(2, "package.source1.java"));
        persistenceEntity.addLine(new Line(3, "package.source1.java"));
        persistenceEntity.addLine(new Line(4, "package.source1.java"));
        persistenceEntity.addLine(new Line(5, "package.source1.java"));

        persistenceEntity.addLine(new Line(1, "package.source2.java"));
        persistenceEntity.addLine(new Line(3, "package.source2.java"));
        persistenceEntity.addLine(new Line(5, "package.source2.java"));
        persistenceEntity.addLine(new Line(6, "package.source2.java"));
        persistenceEntity.addLine(new Line(7, "package.source2.java"));
    }

    @Test
    public void testJaCoCoSAXHandler() throws Exception {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(handler);
        parser.setFeature("http://xml.org/sax/features/validation", false);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        parser.parse("src/test/resources/jacoco.xml");

        assertEquals(10, persistenceEntity.getAllLines().size());
        assertEquals(2, persistenceEntity.getAllLines().stream()
        .filter(line -> !line.getTestCases().isEmpty()).count());
    }
}