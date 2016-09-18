package at.ac.tuwien.ifs.qse.xmlParser;

import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class JaCoCoSAXHandler.
 */
public class JaCoCoSAXHandlerTest {
    private PersistenceEntity persistenceEntity;
    private JaCoCoSAXHandler jaCoCoSAXHandler;
    private JaCoCoRelevanceSAXHandler jaCoCoRelevanceSAXHandler;

    @Before
    public void setUp() throws Exception {
        persistenceEntity = new PersistenceEntity(null, null, null, null);
        at.ac.tuwien.ifs.qse.model.TestCase testCase = new at.ac.tuwien.ifs.qse.model.TestCase("test1.java", true);
        persistenceEntity.getTestCases().add(testCase);

        jaCoCoSAXHandler = new JaCoCoSAXHandler(persistenceEntity, testCase);
        jaCoCoRelevanceSAXHandler = new JaCoCoRelevanceSAXHandler(persistenceEntity);

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
        ParserRunner parserRunner = new ParserRunner();

        parserRunner.runXMLParser(jaCoCoRelevanceSAXHandler, "src/test/resources/jacoco.xml");
        parserRunner.runXMLParser(jaCoCoSAXHandler, "src/test/resources/jacoco.xml");

        assertEquals(12, persistenceEntity.getAllLines().size());
        assertEquals(6, persistenceEntity.getRelevantLines().size());
        assertEquals(3, persistenceEntity.getRelevantLines().stream()
        .filter(line -> !line.getTestCases().isEmpty()).count());
    }
}