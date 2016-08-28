package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.ModelAccessService;
import at.ac.tuwien.ifs.qse.service.TestReportSAXHandler;
import org.apache.maven.cli.MavenCli;
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

    public void analyzeCoverage() {
        try {
            analyseTestReport();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        for (TestCase testCase : testCases.values()) {
            codeCoverageTool.analyseCoverageReport(testCase);
        }
    }

    /**
     * Generates the test report of the project and analyses it.
     */
    private void analyseTestReport() throws SAXException, IOException {
        MavenCli mavenCli = new MavenCli();
        String outputPath = "./target/mavenOutput.txt";

        mavenCli.doMain(new String[]{"test"}, ".", new PrintStream(
                new FileOutputStream(outputPath)), System.out);

        XMLReader parser = XMLReaderFactory.createXMLReader();
        TestReportSAXHandler handler = new TestReportSAXHandler();
        parser.setContentHandler(handler);
        List<String> reports = new ArrayList<>();

        Files.walk(Paths.get("./target/surefire-reports")).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith("xml")) {
                reports.add(filePath.toString());
            }
        });

        for (String report : reports) {
            parser.parse(report);
        }
    }

}
