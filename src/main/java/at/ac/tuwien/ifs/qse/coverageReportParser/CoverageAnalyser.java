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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        LOGGER.info("analyzing coverage...");
        int failureCounter = 0;
        List<Throwable> exceptions = new ArrayList<>();

        LOGGER.info("analyzing test reports...");
        try {
            analyseTestReports();
        } catch (Exception e) {
            failureCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }

        LOGGER.info("identifying relevant lines...");
        try {
            codeCoverageTool.analyseRelevantLines();
        } catch (Exception e) {
            failureCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }

        LOGGER.info("analyzing coverage reports...");
        int numberOfTests = persistence.getTestCases().size();
        int current = 1;

        for (TestCase testCase : persistence.getTestCases()) {
            try {
                LOGGER.info("analysing report for test case " + current++ + "/" + numberOfTests);
                codeCoverageTool.analyseCoverageReport(testCase);
            } catch (Exception e) {
                failureCounter ++;
                exceptions.add(e);
                persistence.setShowWarning(true);
            }
        }

        if (failureCounter != 0) {
            LOGGER.error(failureCounter + " failure(s) happened while analyzing test & coverage reports.");
            LOGGER.error("listing failures");
            exceptions.forEach(throwable -> LOGGER.error(throwable.getMessage(), throwable));
        }
    }

    /**
     * Generates the test report of the project and analyses it.
     */
    private void analyseTestReports() throws SAXException, IOException, MavenInvocationException {

        RemoteMavenRunner.runRemoteMaven(persistence.getTargetProjectPath() + "/pom.xml",
                Arrays.asList("clean", "test", "-q", "-fn", "-fae", "-DfailIfNoTests=false"));

        TestReportSAXHandler handler = new TestReportSAXHandler(persistence);
        List<String> reports =
                Files.walk(Paths.get(persistence.getTargetProjectPath()))
                        .filter(filePath -> Files.isRegularFile(filePath))
                        .filter(filePath -> filePath.toString().matches(".*surefire-reports.*TEST.*xml"))
                        .map(Path::toString)
                        .collect(Collectors.toList());

        ParserRunner.runXMLParser(handler, reports);
    }

}