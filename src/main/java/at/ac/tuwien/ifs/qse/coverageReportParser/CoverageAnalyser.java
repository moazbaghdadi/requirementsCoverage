package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.ModelAccessService;
import at.ac.tuwien.ifs.qse.service.TestReportSAXHandler;
import org.apache.maven.cli.MavenCli;
import org.apache.maven.wagon.observers.Debug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generates for each test case a code coverage report and
 * parses it's information.
 *
 */
public class CoverageAnalyser {
    private CodeCoverageTool codeCoverageTool;
    private Map<String, TestCase> testCases;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoverageAnalyser.class);

    public CoverageAnalyser(CodeCoverageTool codeCoverageTool) {
        this.codeCoverageTool = codeCoverageTool;
        this.testCases = ModelAccessService.getTestCases();
    }

    public void analyzeCoverage(String path) {
        try {
            analyseTestReport(path);
        } catch (Exception e) {
            LOGGER.error("Error while analyzing test reports: " + e.getMessage(), e);
        }
        for (TestCase testCase : testCases.values()) {
            codeCoverageTool.analyseCoverageReport(testCase);
        }
    }

    /**
     * Generates the test report of the project and analyses it.
     */
    private void analyseTestReport(String path) throws SAXException, IOException {
        MavenCli mavenCli = new MavenCli();
        String outputPath = "./target/mavenOutput.txt";

        /*mavenCli.doMain(new String[]{"clean test"}, path, new PrintStream(
                new FileOutputStream(outputPath)),  new PrintStream(
                new FileOutputStream(outputPath)));*/

        mavenCli.doMain(new String[]{"test", "-fae"}, path,
                System.out,  System.out);

        XMLReader parser = XMLReaderFactory.createXMLReader();
        TestReportSAXHandler handler = new TestReportSAXHandler();
        parser.setContentHandler(handler);
        List<String> reports = new ArrayList<>();

        Files.walk(Paths.get(path)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().matches(".*surefire-reports.*TEST.*xml")) {
                reports.add(filePath.toString());
            }
        });

        for (String report : reports) {
            parser.parse(report);
        }
    }

}
