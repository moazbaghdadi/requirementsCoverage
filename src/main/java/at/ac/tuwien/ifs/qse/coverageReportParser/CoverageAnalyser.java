package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.service.RemoteMavenRunner;
import at.ac.tuwien.ifs.qse.xmlParser.ParserRunner;
import at.ac.tuwien.ifs.qse.xmlParser.TestReportSAXHandler;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generates for each test case a code coverage report and
 * parses it's information.
 *
 */
public class CoverageAnalyser {
    private CodeCoverageTool codeCoverageTool;
    private Persistence persistence;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoverageAnalyser.class);

    public CoverageAnalyser(Persistence persistence, CodeCoverageTool codeCoverageTool) {
        this.codeCoverageTool = codeCoverageTool;
        this.persistence = persistence;
    }

    public void analyzeCoverage() {
        LOGGER.info("Starting coverage analyses...");
        try {
            LOGGER.info("analyzing test reports...");
            analyseTestReports();
        } catch (Exception e) {
            LOGGER.error("Error while analyzing test reports: " + e.getMessage(), e);
        }
        LOGGER.info(persistence.toString());

        LOGGER.info("identifying relevant lines...");
        try {
            codeCoverageTool.analyseRelevantLines();
        } catch (Exception e) {
            LOGGER.error("Error while analyzing relevant lines: " + e.getMessage(), e);
        }
        LOGGER.info(persistence.toString());

        LOGGER.info("analyzing coverage reports...");
        int numberOfTests = persistence.getTestCases().size();
        int current = 1;
        for (TestCase testCase : persistence.getTestCases()) {
            try {
                LOGGER.info("analysing report for test case " + current++ + "/" + numberOfTests);
                codeCoverageTool.analyseCoverageReport(testCase);
            } catch (Exception e) {
                LOGGER.error("Error while analyzing coverage report of " + testCase.getTestCaseName() + ": " + e.getMessage(), e);
            }
        }
        LOGGER.info(persistence.toString());
    }

    /**
     * Generates the test report of the project and analyses it.
     */
    private void analyseTestReports() throws SAXException, IOException, MavenInvocationException {

        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("clean", "test", "-q", "-fn", "-fae", "-DfailIfNoTests=false"));

        ParserRunner parserRunner = new ParserRunner();
        TestReportSAXHandler handler = new TestReportSAXHandler(persistence);
        List<String> reports = new ArrayList<>();
        Files.walk(Paths.get(persistence.getTargetProjectPath())).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().matches(".*surefire-reports.*TEST.*xml")) {
                reports.add(filePath.toString());
            }
        });

        parserRunner.runXMLParser(handler, reports);
    }

}
