package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Requirement;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple html report generator.
 */
public class ReportGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportGenerator.class);
    private Persistence persistence;
    private StatisticsCalculator statisticsCalculator;

    public ReportGenerator(Persistence persistence) {
        this.persistence = persistence;
        this.statisticsCalculator = new StatisticsCalculator(persistence);
    }

    public void generateReport() throws IOException {

        if (Files.exists(Paths.get("target/requirementsCoverage"))) {
            List<Path> paths = new ArrayList<>(Files.walk(Paths.get("target/requirementsCoverage"))
                    .filter(Files::isRegularFile).collect(Collectors.toList()));
            for (Path path :
                    paths) {
                Files.delete(path);
            }
        }

        LOGGER.info("generating report...");
        String PATH_TO_REQUIREMENTS = "target/requirementsCoverage/requirements";
        String PATH_TO_ISSUES = "target/requirementsCoverage/requirements/issues";
        Files.createDirectories(Paths.get("target/requirementsCoverage/"));
        File file = new File("target/requirementsCoverage/index.html");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        addHeader(writer);
        addProjectInfo(writer);

        writer.write("</body>\n</html>");
        writer.close();
        Files.createDirectories(Paths.get(PATH_TO_REQUIREMENTS));
        Files.createDirectories(Paths.get(PATH_TO_ISSUES));

        LOGGER.info("printing reports for requirements...");
        file = new File(PATH_TO_REQUIREMENTS + "/notRelatedToRequirement.html");
        writer = new BufferedWriter(new FileWriter(file));
        addHeader(writer);
        addNotRelatedToRequirementPage(writer);
        writer.close();

        for (Requirement requirement : persistence.getRequirements()) {
            file = new File(PATH_TO_REQUIREMENTS + "/" + requirement.getRequirementId() + ".html");
            writer = new BufferedWriter(new FileWriter(file));
            addHeader(writer);
            addRequirementPage(writer, requirement);
            writer.close();
        }

        LOGGER.info("printing issues report...");
        file = new File(PATH_TO_ISSUES + "/notRelatedToIssue.html");
        writer = new BufferedWriter(new FileWriter(file));
        addHeader(writer);
        addNotRelatedToIssuePage(writer);
        writer.close();
        for (Issue issue : persistence.getIssues()) {
            file = new File(PATH_TO_ISSUES + "/" + issue.getIssueId() + ".html");
            writer = new BufferedWriter(new FileWriter(file));
            addHeader(writer);
            addIssuePage(writer, issue.getIssueId());
            writer.close();
        }
        LOGGER.info("report generated under: target/requirementsCoverage");
    }

    private void addNotRelatedToIssuePage(BufferedWriter writer) throws IOException {
        long allLines = statisticsCalculator.countNotRelatedToIssueLines();
        long covered = statisticsCalculator.countNotRelatedToIssueCoveredLines();
        long relevant = statisticsCalculator.countNotRelatedToIssueRelevantLines();
        long positive = statisticsCalculator.countNotRelatedToIssuePositivelyCoveredLines();
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (covered!=0?(double)covered:1);
        double negativeCoverage = 100 * (covered-positive) / (covered!=0?(double)covered:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        List<TestCase> testCases = new ArrayList<>(statisticsCalculator.getNotRelatedToIssuesTestCases());

        writer.write("<h1 style=\"float: none;\" align=\"center\">Not Related To Issues</h1>" +
                "<article style=\"padding-left: 10%\">\n" +
                "<a href=\"../../index.html\">Requirements Coverage Report</a>\t>\t<a href=\"../notRelatedToRequirement"
                + ".html\">Not Related To Requirement</a>\t>\tNot Related To Issue" +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + relevantLinesPercentage +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + totalCoverage +
                "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + positiveCoverage +
                "% (" + positive + " of " + covered + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + negativeCoverage +
                "% (" + (covered - positive) + " of " + covered + " lines)</li>\n" +
                "        <li>Uncovered lines: " + uncoveredPercentage +
                "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of test cases not related to issues: " + testCases.size() + "</li>\n" +
                "    </ul>\n" +
                "    <h2>TestCases:</h2>\n" +
                "    <ul>\n");

        Collections.sort(testCases);
        for (TestCase testCase : testCases) {
            writer.write("        <li>" + testCase.getTestCaseName() + "</li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void addNotRelatedToRequirementPage(BufferedWriter writer) throws IOException {
        long allLines = statisticsCalculator.countNotRelatedToRequirementLines();
        long covered = statisticsCalculator.countNotRelatedToRequirementCoveredLines();
        long relevant = statisticsCalculator.countNotRelatedToRequirementRelevantLines();
        long positive = statisticsCalculator.countNotRelatedToRequirementPositivelyCoveredLines();
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (covered!=0?(double)covered:1);
        double negativeCoverage = 100 * (covered-positive) / (covered!=0?(double)covered:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        List<Issue> issues = new ArrayList<>(persistence.getIssues());
        issues = issues.stream()
                .filter(issue -> issue.getRequirement() == null)
                .collect(Collectors.toList());

        writer.write("<h1 style=\"float: none;\" align=\"center\">NOT RELATED TO REQUIREMENT</h1>" +
                "<article style=\"padding-left: 10%\">\n" +
                "<a href=\"../index.html\">Requirements Coverage Report</a>\t>\tNo Requirement" +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + relevantLinesPercentage +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + totalCoverage +
                "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + positiveCoverage +
                "% (" + positive + " of " + covered + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + negativeCoverage +
                "% (" + (covered - positive) + " of " + covered + " lines)</li>\n" +
                "        <li>Uncovered lines: " + uncoveredPercentage +
                "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of issues not related to requirements: " + issues.size() + "</li>\n" +
                "    </ul>\n" +
                "    <h2>Issues:</h2>\n" +
                "    <ul>\n" +
                "        <li><a href=\"issues/notRelatedToIssue.html\">" +
                "Not Related To Issues</a></li>\n");

        Collections.sort(issues);
        for (Issue issue : issues) {
            writer.write("        <li><a href=\"issues/" + issue.getIssueId() + ".html\">" +
                    issue.getIssueId() + "</a></li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void addRequirementPage(BufferedWriter writer, Requirement requirement) throws IOException {
        long allLines = statisticsCalculator.countLines(requirement);
        long covered = statisticsCalculator.countCoveredLines(requirement);
        long relevant = statisticsCalculator.countRelevantLines(requirement);
        long positive = statisticsCalculator.countPositivelyCoveredLines(requirement);
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (covered!=0?(double)covered:1);
        double negativeCoverage = 100 * (covered-positive) / (covered!=0?(double)covered:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        List<Issue> issues = new ArrayList<>(requirement.getIssues());

        writer.write("<h1 style=\"float: none;\" align=\"center\">" + requirement.getRequirementId().toUpperCase() + "</h1>" +
                "<article style=\"padding-left: 10%\">\n" +
                "<a href=\"../index.html\">Requirements Coverage Report</a>\t>\t" + requirement.getRequirementId() +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + relevantLinesPercentage +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + totalCoverage +
                "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + positiveCoverage +
                "% (" + positive + " of " + covered + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + negativeCoverage +
                "% (" + (covered - positive) + " of " + covered + " lines)</li>\n" +
                "        <li>Uncovered lines: " + uncoveredPercentage +
                "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of issues for this requirement: " + issues.size() + "</li>\n" +
                "    </ul>\n" +
                "    <h2>Issues:</h2>\n" +
                "    <ul>\n");

        Collections.sort(issues);
        for (Issue issue : issues) {
            writer.write("        <li><a href=\"issues/" + issue.getIssueId() + ".html\">" +
                    issue.getIssueId() + "</a></li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void addIssuePage(BufferedWriter writer, String issueId) throws IOException {
        long allLines = statisticsCalculator.countLines(issueId);
        long covered = statisticsCalculator.countCoveredLines(issueId);
        long relevant = statisticsCalculator.countRelevantLines(issueId);
        long positive = statisticsCalculator.countPositivelyCoveredLines(issueId);
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (covered!=0?(double)covered:1);
        double negativeCoverage = 100 * (covered-positive) / (covered!=0?(double)covered:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        Requirement req = persistence.getIssue(issueId).getRequirement();
        String requirement = req!=null?req.getRequirementId():"Not Related To Requirement";
        List<TestCase> testCases = new ArrayList<>(statisticsCalculator.getTestCasesForIssue(issueId));

        writer.write("<h1 style=\"float: none;\" align=\"center\">" + issueId.toUpperCase() + "</h1>" +
                "<article style=\"padding-left: 10%\">\n" +
                "<a href=\"../../index.html\">Requirements Coverage Report</a>\t>\t<a href=\"../" + requirement.replace(" ", "")
                + ".html\">" + requirement + "</a>\t>\t" + issueId +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + relevantLinesPercentage +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
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

        Collections.sort(testCases);
        for (TestCase testCase : testCases) {
            writer.write("        <li>" + testCase.getTestCaseName() + "</li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void addProjectInfo(BufferedWriter writer) throws IOException {
        writer.write("<h1 style=\"float: none;\" align=\"center\">Project Name</h1>");
        writer.write("<article style=\"padding-left: 10%\">\n" +
                "    <h2>Overall Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Number of Java files: " + persistence.getFiles().size() + "</li>\n" +
                "        <li>Number of requirements: " + persistence.getRequirements().size() + "</li>\n" +
                "        <li>Number of issues: " + persistence.getIssues().size() + "</li>\n" +
                "        <li>Number of test cases: " + persistence.getTestCases().size() + "</li>\n" +
                "        <li>Number of positive test cases: " + statisticsCalculator.getNumberOfPositiveTests() + "</li>\n" +
                "        <li>Number of negative test cases: " + (persistence.getTestCases().size() -
                                statisticsCalculator.getNumberOfPositiveTests()) + "</li>\n" +
                "        <li>Number of lines: " + persistence.getAllLines().size() + "</li>\n" +
                "        <li>Number of lines relevant for coverage: " + statisticsCalculator.countRelevantLines() + "</li>\n" +
                "        <li>Number of covered lines: " + statisticsCalculator.countCoveredLines() + "</li>\n" +
                "        <li>Number of positively covered lines: " + statisticsCalculator.countPositivelyCoveredLines() + "</li>\n" +
                "        <li>Number of negatively covered lines: " + (statisticsCalculator.countCoveredLines() -
                                statisticsCalculator.countPositivelyCoveredLines()) + "</li>\n" +
                "    </ul>\n" +
                "    <h2>Requirements:</h2>\n" +
                "    <ul>\n" +
                "        <li><a href=\"requirements/notRelatedToRequirement.html\">" +
                "Not Related To Requirement</a></li>\n");

        List<Requirement> requirements = new ArrayList<>(persistence.getRequirements());
        Collections.sort(requirements);
        for (Requirement requirement : requirements) {
            writer.write("        <li><a href=\"requirements/" + requirement.getRequirementId() +
                    ".html\">" + requirement.getRequirementId() + "</a></li>\n");
        }
        writer.write("    </ul>\n</article>\n");
    }

    private void addHeader(BufferedWriter writer) throws IOException {
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
