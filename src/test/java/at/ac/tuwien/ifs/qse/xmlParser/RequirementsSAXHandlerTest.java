package at.ac.tuwien.ifs.qse.xmlParser;

import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Requirement;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the class RequirementsSAXHandler
 */
public class RequirementsSAXHandlerTest {

    private Persistence persistence;
    private RequirementsSAXHandler requirementsSAXHandler;
    @Before
    public void setUp() throws Exception {
        persistence = new PersistenceEntity(null, null, null, null);
        requirementsSAXHandler = new RequirementsSAXHandler(persistence);

        persistence.addIssue(new Issue("QPID-1028"));
        persistence.addIssue(new Issue("QPID-1062"));
        persistence.addIssue(new Issue("QPID-1079"));

        persistence.addIssue(new Issue("QPID-4454"));
        persistence.addIssue(new Issue("QPID-4432"));

        persistence.addIssue(new Issue("QPID-77"));

        persistence.addIssue(new Issue("QPID-0000"));
    }

    @Test
    public void testRequirementsSAXHandler() throws Exception {
        ParserRunner.runXMLParser(requirementsSAXHandler, "src/test/resources/requirements.xml");

        assertEquals(10, persistence.getIssues().size());
        assertEquals(3, persistence.getRequirements().size());

        assertNotNull(persistence.getIssue("QPID-1028").getRequirement());
        assertNull(persistence.getIssue("QPID-0000").getRequirement());

        for (Requirement requirement :
                persistence.getRequirements()) {
            assertEquals(3, requirement.getIssues().size());
        }
    }
}