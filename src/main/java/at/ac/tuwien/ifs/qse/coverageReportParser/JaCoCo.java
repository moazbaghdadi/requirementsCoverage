package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.service.RemoteMavenRunner;
import at.ac.tuwien.ifs.qse.xmlParser.JaCoCoRelevanceSAXHandler;
import at.ac.tuwien.ifs.qse.xmlParser.JaCoCoSAXHandler;
import at.ac.tuwien.ifs.qse.xmlParser.ParserRunner;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JaCoCo implements CodeCoverageTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(JaCoCo.class);
    private Persistence persistence;

    public JaCoCo (Persistence persistence){
        this.persistence = persistence;
    }

    public void analyseRelevantLines() throws IOException, SAXException, MavenInvocationException {
        LOGGER.info("running maven goal: mvn clean test -fn -fae -q -DfailIfNoTests=false");
        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("clean", "test", "-fn", "-fae", "-q", "-DfailIfNoTests=false"));

        LOGGER.info("running maven goal: jacoco:report -q");
        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("jacoco:report", "-q"));

        JaCoCoRelevanceSAXHandler handler = new JaCoCoRelevanceSAXHandler(persistence);

        LOGGER.info("parsing coverage report...");
        ParserRunner.runXMLParser(handler, getReports());
    }

    public void analyseCoverageReport(TestCase testCase) throws IOException, SAXException, MavenInvocationException {
        LOGGER.info("running maven goal: mvn clean test -fn -fae -q -Dtest=\"" + testCase.getTestCaseName() +
                "\" -DfailIfNoTests=false");
        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("clean", "test", "-fn", "-fae","-q", "-Dtest=\"" + testCase.getTestCaseName()
                        + "\"", "-DfailIfNoTests=false"));

        LOGGER.info("running maven goal: jacoco:report -q");
        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("jacoco:report", "-q"));

        JaCoCoSAXHandler handler = new JaCoCoSAXHandler(persistence, testCase);

        LOGGER.info("parsing coverage report...");
        ParserRunner.runXMLParser(handler, getReports());
    }

    private List<String> getReports() throws IOException {
        return Files.walk(Paths.get(persistence.getTargetProjectPath()))
                .filter(filePath -> Files.isRegularFile(filePath))
                .filter(filePath -> filePath.toString().matches(".*target.*site.*jacoco.*jacoco.xml"))
                .map(Path::toString)
                .collect(Collectors.toList());
    }
}
