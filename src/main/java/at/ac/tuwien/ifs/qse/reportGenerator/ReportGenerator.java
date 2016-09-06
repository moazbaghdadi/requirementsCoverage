package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.PersistenceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Simple html report generator.
 */
public class ReportGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGenerator.class);
    private PersistenceEntity persistenceEntity;
    private StatisticsCalculator statisticsCalculator;

    public ReportGenerator(PersistenceEntity persistenceEntity) {
        this.persistenceEntity = persistenceEntity;
        this.statisticsCalculator = new StatisticsCalculator(persistenceEntity);
    }

    public void printOutStatistics() throws IOException {

        LOGGER.info("printing report");
        Files.createDirectories(Paths.get("target/requirementsCoverage/"));
        File file = new File("target/requirementsCoverage/index.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        printHeader(writer);
        printProjectInfo(writer);

        writer.write("</body>\n</html>");
        writer.close();

        Files.createDirectories(Paths.get("target/requirementsCoverage/issues"));
        LOGGER.info("printing issues report...");
        for (String issueId :
                persistenceEntity.getIssues().keySet()) {
            file = new File("target/requirementsCoverage/issues/" + issueId + ".html");
            writer = new BufferedWriter(new FileWriter(file));
            printHeader(writer);
            printIssuePage(writer, issueId);
            writer.close();
        }
        LOGGER.info("report generated under: target/requirementsCoverage");
    }

    private void printIssuePage(BufferedWriter writer, String issueId) throws IOException {
        long covered = statisticsCalculator.countCoveredLines(issueId);
        long relevant = statisticsCalculator.countRelevantLines(issueId);
        long positive = statisticsCalculator.countPositivelyCoveredLines(issueId);
        long uncovered = relevant - covered;
        double totalCoverage = 100 * covered / (relevant!=0?relevant:1);
        double positiveCoverage = 100 * positive / (covered!=0?covered:1);
        double negativeCoverage = 100 * (covered-positive) / (covered!=0?covered:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?relevant:1);
        Set<TestCase> testCases = statisticsCalculator.getTestCasesForIssue(issueId);

        writer.write("<article style=\"padding-left: 10%\">\n" +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Total coverage: " + totalCoverage +
                    "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + positiveCoverage +
                    "% (" + positive + " of " + covered + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + negativeCoverage +
                    "% (" + (covered - positive) + " of " + covered + " lines)</li>\n" +
                "        <li>Uncovered lines: " + uncoveredPercentage +
                    "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of test cases covering this issue: " + testCases.size() + "</li>\n" +
                "    </ul>\n" +
                "    <h2>TestCases:</h2>\n" +
                "    <ul>\n");
        for (TestCase testCase : testCases) {
            writer.write("        <li>" + testCase.getTestCaseName() + "</li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void printProjectInfo(BufferedWriter writer) throws IOException {
        writer.write("<h1 style=\"float: none;\" align=\"center\">Project Name</h1>");
        writer.write("<article style=\"padding-left: 10%\">\n" +
                "    <h2>Overall Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Number of files: " + persistenceEntity.getFiles().size() + "</li>\n" +
                "        <li>Number of issues: " + persistenceEntity.getIssues().size() + "</li>\n" +
                "        <li>Number of test cases: " + persistenceEntity.getTestCases().size() + "</li>\n" +
                "        <li>Number of positive test cases: " + statisticsCalculator.getNumberOfPositiveTests() + "</li>\n" +
                "        <li>Number of lines: " + persistenceEntity.getLines().size() + "</li>\n" +
                "        <li>Number of lines relevant for coverage: " + statisticsCalculator.countRelevantLines() + "</li>\n" +
                "        <li>Number of covered lines: " + statisticsCalculator.countCoveredLines() + "</li>\n" +
                "        <li>Number of positively covered lines: " + statisticsCalculator.countPositivelyCoveredLines() + "</li>\n" +
                "    </ul>\n" +
                "    <h2>Issues:</h2>\n" +
                "    <ul>\n");
        List<String> issues = new ArrayList<>(persistenceEntity.getIssues().keySet());
        Collections.sort(issues);
        for (String issueId : issues) {
            writer.write("        <li><a href=\"issues/" + issueId + ".html\">" + issueId + "</a></li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void printHeader(BufferedWriter writer) throws IOException {
        writer.write("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Requirements Coverage Report</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<header style=\"width: 100%;height: 70px;background-color: red;background: -webkit-gradient(linear,left top,left bottom,from(red),to(#840e12));\">\n" +
                "    <p style=\"float: left;margin: 22px;font-family: sans-serif;font-size: 1.3em;color:white;text-shadow: black 1px 1px 0;\">Requirements Coverage Report</p>\n" +
                "    <p style=\"float: right;margin: 22px;font-family: sans-serif;font-size: 1.3em;color:white;text-shadow: black 1px 1px 0;\">Faculty of Informatics</p>\n" +
                "</header>");
    }

}
