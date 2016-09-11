package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.service.JaCoCoSAXHandler;
import at.ac.tuwien.ifs.qse.service.RemoteMavenRunner;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JaCoCo implements CodeCoverageTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(JaCoCo.class);
    private Persistence persistence;

    public JaCoCo (Persistence persistence){
        this.persistence = persistence;
    }

    public void analyseCoverageReport(TestCase testCase) throws IOException, SAXException, MavenInvocationException {
        LOGGER.info("running maven goal: mvn clean test -q -Dtest=\"" + testCase.getTestCaseName() +
                ".java\" -DfailIfNoTests=false");
        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("clean", "test", "-q", "-Dtest=\"" + testCase.getTestCaseName()
                        +".java\"", "-DfailIfNoTests=false"));

        LOGGER.info("running maven goal: jacoco:report -q");
        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("jacoco:report", "-q"));

        LOGGER.info("parsing coverage report...");
        parseJaCoCoReport(testCase);
    }

    private void parseJaCoCoReport(TestCase testCase) throws SAXException, IOException {

        List<String> reports = new ArrayList<>();
        Files.walk(Paths.get(persistence.getTargetProjectPath())).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().matches(".*target.*site.*jacoco.*jacoco.xml")) {
                reports.add(filePath.toString());
            }
        });

        XMLReader parser = XMLReaderFactory.createXMLReader();
        JaCoCoSAXHandler handler = new JaCoCoSAXHandler(persistence, testCase);
        parser.setContentHandler(handler);
        parser.setFeature("http://xml.org/sax/features/validation", false);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        for (String report : reports) {
            parser.parse(report);
        }
    }
}
