package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.service.RemoteMavenRunner;
import at.ac.tuwien.ifs.qse.service.TestReportSAXHandler;
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
import java.util.Set;

/**
 * Generates for each test case a code coverage report and
 * parses it's information.
 *
 */
public class CoverageAnalyser {
    private CodeCoverageTool codeCoverageTool;
    private Set<TestCase> testCases;
    private Persistence persistence;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoverageAnalyser.class);

    public CoverageAnalyser(Persistence persistence, CodeCoverageTool codeCoverageTool) {
        this.codeCoverageTool = codeCoverageTool;
        this.persistence = persistence;
        this.testCases = this.persistence.getTestCases();
    }

    public void analyzeCoverage() {
        LOGGER.info("Starting coverage analyses...");
        try {
            LOGGER.info("analyzing test reports...");
            analyseTestReports();
        } catch (Exception e) {
            LOGGER.error("Error while analyzing test reports: " + e.getMessage(), e);
        }

        LOGGER.info("analyzing Coverage reports...");
        for (TestCase testCase : testCases) {
            try {
                codeCoverageTool.analyseCoverageReport(testCase);
            } catch (Exception e) {
                LOGGER.error("Error while analyzing coverage report of " + testCase.getTestCaseName() + ": " + e.getMessage(), e);
            }
        }
    }

    /**
     * Generates the test report of the project and analyses it.
     */
    private void analyseTestReports() throws SAXException, IOException, MavenInvocationException {

        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("clean", "test", "-q", "-fn", "-fae", "-DfailIfNoTests=false"));

        XMLReader parser = XMLReaderFactory.createXMLReader();
        TestReportSAXHandler handler = new TestReportSAXHandler(persistence);
        parser.setContentHandler(handler);
        List<String> reports = new ArrayList<>();

        Files.walk(Paths.get(persistence.getTargetProjectPath())).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().matches(".*surefire-reports.*TEST.*xml")) {
                reports.add(filePath.toString());
            }
        });

        LOGGER.info("parsing " + reports.size() + " test reports...");
        for (String report : reports) {
            parser.parse(report);
        }
        LOGGER.info(persistence.getTestCases().size() + " test cases were successfully parsed of test reports.");
    }

}
