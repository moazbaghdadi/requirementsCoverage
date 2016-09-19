package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Requirement;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
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
    private final String PATH_TO_REPORT = "target/requirementsCoverage";
    private final String PATH_TO_REQUIREMENTS = "target/requirementsCoverage/requirements";
    private final String PATH_TO_ISSUES = "target/requirementsCoverage/requirements/issues";

    public ReportGenerator(Persistence persistence) {
        this.persistence = persistence;
        this.statisticsCalculator = new StatisticsCalculator(persistence);
    }

    /**
     * Generates the requirements coverage report. This method keeps printing the report even
     * if it fails in some points. If any failures happens, they are listed after generating
     * the report.
     */
    public void generateReport() {
        LOGGER.info("generating report...");

        int failuresCounter = 0;
        List<Throwable> exceptions = new ArrayList<>();

        LOGGER.info("preparing folders' structure");
        try {
            if (Files.exists(Paths.get(PATH_TO_REPORT))) {
                List<Path> paths = new ArrayList<>(Files.walk(Paths.get(PATH_TO_REPORT))
                        .filter(Files::isRegularFile).collect(Collectors.toList()));
                for (Path path :
                        paths) {
                    Files.delete(path);
                }
            }
            Files.createDirectories(Paths.get(PATH_TO_REPORT));
            Files.createDirectories(Paths.get(PATH_TO_REQUIREMENTS));
            Files.createDirectories(Paths.get(PATH_TO_REQUIREMENTS + "/img"));
            Files.createDirectories(Paths.get(PATH_TO_ISSUES));
            Files.createDirectories(Paths.get(PATH_TO_ISSUES + "/img"));
        } catch (IOException e) {
            failuresCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }

        LOGGER.info("generating reports for requirements...");

        File file = new File(PATH_TO_REPORT + "/index.html");
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            addHeader(writer, "style.css");
            addGeneralProjectInformationPage(writer);
            writer.write("</body>\n</html>");
            writer.close();
        } catch (IOException e) {
            failuresCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }

        file = new File(PATH_TO_REQUIREMENTS + "/notRelatedToRequirement.html");
        try {
            writer = new BufferedWriter(new FileWriter(file));
            addHeader(writer, "../style.css");
            addNotRelatedToRequirementPage(writer);
            writer.write("</body>\n</html>");
            writer.close();
        } catch (IOException e) {
            failuresCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }

        for (Requirement requirement : persistence.getRequirements()) {
            file = new File(PATH_TO_REQUIREMENTS + "/" + requirement.getRequirementId() + ".html");
            try {
                writer = new BufferedWriter(new FileWriter(file));
                addHeader(writer, "../style.css");
                addRequirementPage(writer, requirement);
                writer.write("</body>\n</html>");
                writer.close();
            } catch (IOException e) {
                failuresCounter ++;
                exceptions.add(e);
                persistence.setShowWarning(true);
            }
        }

        LOGGER.info("printing issues report...");
        file = new File(PATH_TO_ISSUES + "/notRelatedToIssue.html");
        try {
            writer = new BufferedWriter(new FileWriter(file));
            addHeader(writer, "../../style.css");
            addNotRelatedToIssuePage(writer);
            writer.write("</body>\n</html>");
            writer.close();
        } catch (IOException e) {
            failuresCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }
        for (Issue issue : persistence.getIssues()) {
            file = new File(PATH_TO_ISSUES + "/" + issue.getIssueId() + ".html");
            try {
                writer = new BufferedWriter(new FileWriter(file));
                addHeader(writer, "../../style.css");
                addIssuePage(writer, issue.getIssueId());
                writer.write("</body>\n</html>");
                writer.close();
            } catch (IOException e) {
                failuresCounter ++;
                exceptions.add(e);
                persistence.setShowWarning(true);
            }
        }
        try {
            generateStyle();
        } catch (IOException e) {
            failuresCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }
        LOGGER.info("report generated under: " + PATH_TO_REPORT);

        if (failuresCounter != 0) {
            LOGGER.error(failuresCounter + " failure(s) happened while generating the report pages.");
            LOGGER.error("listing failures...");
            exceptions.forEach(throwable -> LOGGER.error(throwable.getMessage(), throwable));
        }
    }

    /**
     * Generates style.css
     * @throws IOException If an I/O error occurs
     */
    private void generateStyle() throws IOException {
        File file = new File(PATH_TO_REPORT + "/style.css");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("body{\n" +
                "    width: 100%;\n" +
                "    margin: 0px;\n" +
                "}\n" +
                "header {\n" +
                "    width: 100%;\n" +
                "    height: 70px;\n" +
                "    background-color: red;\n" +
                "    background: -webkit-gradient(\n" +
                "            linear,left top,left bottom,from(red),to(#840e12)\n" +
                "    );\n" +
                "}\n" +
                "header > p {\n" +
                "    margin: 22px;\n" +
                "    font-family: sans-serif;\n" +
                "    font-size: 1.3em;\n" +
                "    color: white;\n" +
                "    text-shadow: black 1px 1px 0;\n" +
                "}\n" +
                ".statistics li {\n" +
                "    padding-top: 6px;\n" +
                "}\n" +
                ".statistics-main {\n" +
                "    padding-left: 10%;\n" +
                "    width: 40%;\n" +
                "}" +
                ".results li {\n" +
                "    padding-top: 4px;\n" +
                "}\n" +
                ".left {\n" +
                "    float: left;\n" +
                "}\n" +
                ".right {\n" +
                "    float: right;\n" +
                "}\n" +
                ".statistics {\n" +
                "    padding-left: 10%;\n" +
                "    width: 50%;\n" +
                "    height: 300px;\n" +
                "}\n" +
                "\n" +
                ".image {\n" +
                "    padding-right: 10%;\n" +
                "}\n" +
                ".results {\n" +
                "    padding-left: 10%;\n" +
                "}\n" +
                ".main {\n" +
                "    padding-top: 10px;\n" +
                "    width: 100%" +
                "}\n" +
                ".error {\n" +
                "    color: red;\n" +
                "}\n" +
                ".hidden {\n" +
                "    visibility: " + (persistence.showWarning()?"visible":"hidden") + ";\n" +
                "}");
        writer.close();
    }

    /**
     * Writes the html content for the given {@code requirement} on the given {@code writer}.
     * @param writer The writer to write the html content on.
     * @param requirement The requirement to calculate statistics and generate the html page for.
     * @throws IOException If an I/O error occurs
     */
    private void addRequirementPage(BufferedWriter writer, Requirement requirement) throws IOException {
        long allLines = statisticsCalculator.countLines(requirement);
        long covered = statisticsCalculator.countCoveredLines(requirement);
        long relevant = statisticsCalculator.countRelevantLines(requirement);
        long positive = statisticsCalculator.countPositivelyCoveredLines(requirement);
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (relevant!=0?(double)relevant:1);
        double negativeCoverage = 100 * (covered-positive) / (relevant!=0?(double)relevant:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        List<Issue> issues = new ArrayList<>(requirement.getIssues());

        JFreeChart chart = createChart(requirement.getRequirementId(), positiveCoverage, negativeCoverage, uncoveredPercentage);
        ChartUtilities.saveChartAsJPEG(new File(PATH_TO_REQUIREMENTS + "/img/" + requirement.getRequirementId() + ".jpeg")
                , chart, 300, 300);

        writer.write("<h1 align=\"center\">" + requirement.getRequirementId().toUpperCase() + "</h1>" +
                "<div class=\"statistics left\">\n" +
                "<a href=\"../index.html\">Requirements Coverage Report</a>\t>\t" + requirement.getRequirementId() +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + String.format("%.2f", relevantLinesPercentage) +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + String.format("%.2f", totalCoverage) +
                "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + String.format("%.2f", positiveCoverage) +
                "% (" + positive + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + String.format("%.2f", negativeCoverage) +
                "% (" + (covered - positive) + " of " + relevant + " lines)</li>\n" +
                "        <li>Uncovered lines: " + String.format("%.2f", uncoveredPercentage) +
                "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of issues for this requirement: " + issues.size() + "</li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "<div class=\"image right\">\n" +
                "    <img src=\"img/" + requirement.getRequirementId() + ".jpeg\" />\n" +
                "</div>" +
                "<div class=\"results left\">" +
                "    <h2>Issues:</h2>\n" +
                "    <ul>\n");

        Collections.sort(issues);
        for (Issue issue : issues) {
            writer.write("        <li><a href=\"issues/" + issue.getIssueId() + ".html\">" +
                    issue.getIssueId() + "</a></li>\n");
        }
        writer.write("    </ul>\n</div>\n");
    }

    /**
     * Writes the html content for the given {@code issueId} on the given {@code writer}.
     * @param writer The writer to write the html content on.
     * @param issueId The id of the issue to calculate statistics and generate the html page for.
     * @throws IOException If an I/O error occurs
     */
    private void addIssuePage(BufferedWriter writer, String issueId) throws IOException {
        long allLines = statisticsCalculator.countLines(issueId);
        long relevant = statisticsCalculator.countRelevantLines(issueId);
        long covered = statisticsCalculator.countCoveredLines(issueId);
        long positive = statisticsCalculator.countPositivelyCoveredLines(issueId);
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (relevant!=0?(double)relevant:1);
        double negativeCoverage = 100 * (covered-positive) / (relevant!=0?(double)relevant:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        Requirement req = persistence.getIssue(issueId).getRequirement();
        String requirement = req!=null?req.getRequirementId():"Not Related To Requirement";
        List<TestCase> testCases = new ArrayList<>(statisticsCalculator.getTestCasesForIssue(issueId));

        JFreeChart chart = createChart(issueId, positiveCoverage, negativeCoverage, uncoveredPercentage);
        ChartUtilities.saveChartAsJPEG(new File(PATH_TO_ISSUES + "/img/" + issueId + ".jpeg"), chart, 300, 300);

        writer.write("<h1 align=\"center\">" + issueId.toUpperCase() + "</h1>" +
                "<div class=\"statistics left\">\n" +
                "<a href=\"../../index.html\">Requirements Coverage Report</a>\t>\t<a href=\"../" + requirement.replace(" ", "")
                + ".html\">" + requirement + "</a>\t>\t" + issueId +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + String.format("%.2f", relevantLinesPercentage) +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + String.format("%.2f", totalCoverage) +
                    "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + String.format("%.2f", positiveCoverage) +
                    "% (" + positive + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + String.format("%.2f", negativeCoverage) +
                    "% (" + (covered - positive) + " of " + relevant + " lines)</li>\n" +
                "        <li>Uncovered lines: " + String.format("%.2f", uncoveredPercentage) +
                    "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of test cases covering this issue: " + testCases.size() + "</li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "<div class=\"image right\">\n" +
                "    <img src=\"img/" + issueId + ".jpeg\" />\n" +
                "</div>" +
                "<div class=\"results left\">" +
                "    <h2>TestCases:</h2>\n" +
                "    <ul>\n");

        Collections.sort(testCases);
        for (TestCase testCase : testCases) {
            writer.write("        <li>" + testCase.getTestCaseName() + "</li>\n");
        }
        writer.write("    </ul>\n</div>");
    }

    /**
     * Writes the html content for the Not Related To Requirement page on the given {@code writer}.
     * @param writer The writer to write the html content on.
     * @throws IOException If an I/O error occurs
     */
    private void addNotRelatedToRequirementPage(BufferedWriter writer) throws IOException {
        long allLines = statisticsCalculator.countNotRelatedToRequirementLines();
        long covered = statisticsCalculator.countNotRelatedToRequirementCoveredLines();
        long relevant = statisticsCalculator.countNotRelatedToRequirementRelevantLines();
        long positive = statisticsCalculator.countNotRelatedToRequirementPositivelyCoveredLines();
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (relevant!=0?(double)relevant:1);
        double negativeCoverage = 100 * (covered-positive) / (relevant!=0?(double)relevant:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        List<Issue> issues = new ArrayList<>(persistence.getIssues());
        issues = issues.stream()
                .filter(issue -> issue.getRequirement() == null)
                .collect(Collectors.toList());

        JFreeChart chart = createChart("not related to requirements", positiveCoverage, negativeCoverage, uncoveredPercentage);
        ChartUtilities.saveChartAsJPEG(new File(PATH_TO_REQUIREMENTS + "/img/notRelatedToRequirements.jpeg"), chart, 300, 300);

        writer.write("<h1 align=\"center\">NOT RELATED TO REQUIREMENT</h1>" +
                "<div class=\"statistics left\">\n" +
                "<a href=\"../index.html\">Requirements Coverage Report</a>\t>\tNot Related To Requirement" +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + String.format("%.2f", relevantLinesPercentage) +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + String.format("%.2f", totalCoverage) +
                "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + String.format("%.2f", positiveCoverage) +
                "% (" + positive + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + String.format("%.2f", negativeCoverage) +
                "% (" + (covered - positive) + " of " + relevant + " lines)</li>\n" +
                "        <li>Uncovered lines: " + String.format("%.2f", uncoveredPercentage) +
                "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of issues not related to requirements: " + issues.size() + "</li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "<div class=\"image right\">\n" +
                "    <img src=\"img/notRelatedToRequirements.jpeg\" />\n" +
                "</div>" +
                "<div class=\"results left\">" +
                "    <h2>Issues:</h2>\n" +
                "    <ul>\n" +
                "        <li><a href=\"issues/notRelatedToIssue.html\">" +
                "Not Related To Issues</a></li>\n");

        Collections.sort(issues);
        for (Issue issue : issues) {
            writer.write("        <li><a href=\"issues/" + issue.getIssueId() + ".html\">" +
                    issue.getIssueId() + "</a></li>\n");
        }
        writer.write("    </ul>\n</div>\n");
    }

    /**
     * Writes the html content for the Not Related To Issue page on the given {@code writer}.
     * @param writer The writer to write the html content on.
     * @throws IOException If an I/O error occurs
     */
    private void addNotRelatedToIssuePage(BufferedWriter writer) throws IOException {
        long allLines = statisticsCalculator.countNotRelatedToIssueLines();
        long covered = statisticsCalculator.countNotRelatedToIssueCoveredLines();
        long relevant = statisticsCalculator.countNotRelatedToIssueRelevantLines();
        long positive = statisticsCalculator.countNotRelatedToIssuePositivelyCoveredLines();
        long uncovered = relevant - covered;
        double relevantLinesPercentage = 100 * relevant / (allLines!=0?(double)allLines:1);
        double totalCoverage = 100 * covered / (relevant!=0?(double)relevant:1);
        double positiveCoverage = 100 * positive / (relevant!=0?(double)relevant:1);
        double negativeCoverage = 100 * (covered-positive) / (relevant!=0?(double)relevant:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);
        List<TestCase> testCases = new ArrayList<>(statisticsCalculator.getNotRelatedToIssuesTestCases());

        JFreeChart chart = createChart("not related to issue", positiveCoverage, negativeCoverage, uncoveredPercentage);
        ChartUtilities.saveChartAsJPEG(new File(PATH_TO_ISSUES + "/img/notRelatedToIssue.jpeg"), chart, 300, 300);

        writer.write("<h1 align=\"center\">Not Related To Issue</h1>" +
                "<div class=\"statistics left\">\n" +
                "<a href=\"../../index.html\">Requirements Coverage Report</a>\t>\t<a href=\"../notRelatedToRequirement"
                + ".html\">Not Related To Requirement</a>\t>\tNot Related To Issue" +
                "    <h2>Coverage Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Lines relevant for Coverage: " + String.format("%.2f", relevantLinesPercentage) +
                "% (" + relevant + " of " + allLines + " lines)</li>\n" +
                "        <li>Total coverage: " + String.format("%.2f", totalCoverage) +
                "% (" + covered + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by positive test cases: " + String.format("%.2f", positiveCoverage) +
                "% (" + positive + " of " + relevant + " lines)</li>\n" +
                "        <li>Coverage by negative test cases: " + String.format("%.2f", negativeCoverage) +
                "% (" + (covered - positive) + " of " + relevant + " lines)</li>\n" +
                "        <li>Uncovered lines: " + String.format("%.2f", uncoveredPercentage) +
                "% (" + uncovered + " of " + relevant + " lines)</li>\n" +
                "        <li>Number of test cases not related to issues: " + testCases.size() + "</li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "<div class=\"image right\">\n" +
                "    <img src=\"img/notRelatedToIssue.jpeg\" />\n" +
                "</div>\n" +
                "<div class=\"results left\">" +
                "    <h2>TestCases:</h2>\n" +
                "    <ul>\n");

        Collections.sort(testCases);
        for (TestCase testCase : testCases) {
            writer.write("        <li>" + testCase.getTestCaseName() + "</li>\n");
        }
        writer.write("    </ul>\n</div>\n");
    }

    /**
     * Writes the html content for the General Project Information page on the given {@code writer}.
     * @param writer The writer to write the html content on.
     * @throws IOException If an I/O error occurs
     */
    private void addGeneralProjectInformationPage(BufferedWriter writer) throws IOException {
        long allLines = persistence.getAllLines().size();
        long relevant = statisticsCalculator.countRelevantLines();
        long covered = statisticsCalculator.countCoveredLines();
        long positive = statisticsCalculator.countPositivelyCoveredLines();
        long uncovered = relevant - covered;
        double positiveCoverage = 100 * positive / (relevant!=0?(double)relevant:1);
        double negativeCoverage = 100 * (covered-positive) / (relevant!=0?(double)relevant:1);
        double uncoveredPercentage = 100 * uncovered / (relevant!=0?(double)relevant:1);

        JFreeChart chart = createChart("not related to issue", positiveCoverage, negativeCoverage, uncoveredPercentage);
        ChartUtilities.saveChartAsJPEG(new File(PATH_TO_REPORT + "/projectCoverage.jpeg"), chart, 300, 300);

        writer.write("<h1 align=\"center\">Project Name</h1>");
        writer.write("<div class=\"statistics-main left\">\n" +
                "    <h2>Overall Statistics:</h2>\n" +
                "    <ul>\n" +
                "        <li>Number of Java files: " + persistence.getFiles().size() + "</li>\n" +
                "        <li>Number of requirements: " + persistence.getRequirements().size() + "</li>\n" +
                "        <li>Number of issues: " + persistence.getIssues().size() + "</li>\n" +
                "        <li>Number of test cases: " + persistence.getTestCases().size() + "</li>\n" +
                "        <li>Number of positive test cases: " + statisticsCalculator.getNumberOfPositiveTests() + "</li>\n" +
                "        <li>Number of negative test cases: " + (persistence.getTestCases().size() -
                                statisticsCalculator.getNumberOfPositiveTests()) + "</li>\n" +
                "        <li>Number of lines: " + allLines + "</li>\n" +
                "        <li>Number of lines relevant for coverage: " + relevant + "</li>\n" +
                "        <li>Number of covered lines: " + covered + "</li>\n" +
                "        <li>Number of positively covered lines: " + positive + "</li>\n" +
                "        <li>Number of negatively covered lines: " + (covered - positive) + "</li>\n" +
                "        <li>Number of uncovered lines: " + uncovered + "</li>\n" +
                "    </ul>\n" +
                "</div>\n" +
                "<div class=\"image right\">\n" +
                "    <img src=\"projectCoverage.jpeg\" />\n" +
                "</div>\n" +
                "<div class=\"results main left\">" +
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
        writer.write("    </ul>\n</div>\n");
    }

    /**
     * Writes the html content of the header of a page.
     * @param writer The writer to write the html content on.
     * @throws IOException If an I/O error occurs
     */
    private void addHeader(BufferedWriter writer, String pathToCss) throws IOException {
        writer.write("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Requirements Coverage Report</title>\n" +
                "    <link rel=\"stylesheet\" href=\"" + pathToCss +"\">" +
                "</head>\n" +
                "<body>\n" +
                "<header>\n" +
                "    <p class=\"left\">Requirements Coverage Report</p>\n" +
                "    <p class=\"right\">Faculty of Informatics</p>\n" +
                "</header>");
    }

    /**
     * Generates a pie chart.
     * @param title The title of the chart.
     * @param positiveCoverage Percentage of the lines covered by positive test cases.
     * @param negativeCoverage Percentage of the lines covered by negative test cases.
     * @param uncoveredPercentage Percentage of the lines uncovered by test cases.
     * @return the generated pie chart.
     */
    private JFreeChart createChart(String title,
                                   double positiveCoverage,
                                   double negativeCoverage,
                                   double uncoveredPercentage) {
        DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("relevant lines covered by positive tests", positiveCoverage);
        dataSet.setValue("relevant lines covered by negative tests", negativeCoverage);
        dataSet.setValue("relevant lines uncovered", uncoveredPercentage);
        JFreeChart chart = ChartFactory.createPieChart(title, dataSet, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setInteriorGap(0.0);
        plot.setLabelGenerator(null);
        plot.setSectionPaint("relevant lines covered by positive tests", Color.green);
        plot.setSectionPaint("relevant lines covered by negative tests", Color.red);
        plot.setSectionPaint("relevant lines uncovered", Color.gray);
        return chart;
    }
}
