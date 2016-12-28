package at.ac.tuwien.ifs.qse.requirementsParser;

import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.xmlParser.JiraSAXHandler;
import at.ac.tuwien.ifs.qse.xmlParser.ParserRunner;

/**
 * Parses the file containing the requirements of the target project.
 */
public class JiraRequirementsParser {
    private Persistence persistence;

    public JiraRequirementsParser(Persistence persistence) {
        this.persistence = persistence;
    }

    public void parseRequirements() throws Exception {
        JiraSAXHandler jiraSAXHandler = new JiraSAXHandler(persistence);
        ParserRunner.runXMLParser(jiraSAXHandler, persistence.getRequirementsPath());
    }
}
