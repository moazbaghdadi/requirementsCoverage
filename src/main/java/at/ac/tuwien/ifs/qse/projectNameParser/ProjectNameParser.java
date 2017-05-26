package at.ac.tuwien.ifs.qse.projectNameParser;

import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.xmlParser.ParserRunner;
import at.ac.tuwien.ifs.qse.xmlParser.PomSAXHandler;

public class ProjectNameParser {
    private Persistence persistence;

    public ProjectNameParser(Persistence persistence) {
        this.persistence = persistence;
    }

    public void parseProjectName() throws Exception {
        PomSAXHandler pomSAXHandler = new PomSAXHandler(persistence);
        ParserRunner.runXMLParser(pomSAXHandler, persistence.getTargetProjectPath() + "/pom.xml");
    }
}
