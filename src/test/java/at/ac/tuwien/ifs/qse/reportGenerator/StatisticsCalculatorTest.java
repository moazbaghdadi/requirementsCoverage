package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.service.PersistenceEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class StatisticsCalculatorTest.
 */
public class StatisticsCalculatorTest{

    private StatisticsCalculator statisticsCalculator;

    @Before
    public void setUp() throws Exception {
        PersistenceEntity persistenceEntity = new PersistenceEntity(null, null, null);

        Map<String, Issue> issues = persistenceEntity.getIssues();
        issues.put("issue 1", new Issue("issue 1"));
        issues.put("issue 2", new Issue("issue 2"));
        issues.put("issue 3", new Issue("issue 3"));
        issues.put("issue 4", new Issue("issue 4"));
        issues.put("issue 5", new Issue("issue 5"));
        issues.put("issue 6", new Issue("issue 6"));

        Map<String, at.ac.tuwien.ifs.qse.model.TestCase> testCases = persistenceEntity.getTestCases();
        testCases.put("test1", new at.ac.tuwien.ifs.qse.model.TestCase("test1", true));
        testCases.put("test2", new at.ac.tuwien.ifs.qse.model.TestCase("test2", true));
        testCases.put("test3", new at.ac.tuwien.ifs.qse.model.TestCase("test3", true));

        testCases.put("test4", new at.ac.tuwien.ifs.qse.model.TestCase("test4", false));
        testCases.put("test5", new at.ac.tuwien.ifs.qse.model.TestCase("test5", false));
        testCases.put("test6", new at.ac.tuwien.ifs.qse.model.TestCase("test6", false));

        Map<String, File> files = persistenceEntity.getFiles();
        files.put("file1", new File("file1"));
        files.put("file2", new File("file2"));
        files.put("file3", new File("file3"));
        files.put("file4", new File("file4"));
        files.put("file5", new File("file5"));
        files.put("file6", new File("file6"));
        files.put("file7", new File("file7"));

        List<Line> lines = persistenceEntity.getLines();

        Line line = new Line(1, "file1");
        line.setRelevant(true);
        line.setIssueId("issue 1");
        line.addTestCase(testCases.get("test1"));
        lines.add(line);
        line = new Line(2, "file1");
        line.setRelevant(true);
        line.setIssueId("issue 1");
        lines.add(line);
        line = new Line(3, "file1");
        line.setRelevant(true);
        line.setIssueId("issue 1");
        line.addTestCase(testCases.get("test4"));
        lines.add(line);

        line = new Line(1, "file2");
        line.setRelevant(true);
        line.setIssueId("issue 2");
        line.addTestCase(testCases.get("test1"));
        lines.add(line);

        line = new Line(1, "file3");
        line.setRelevant(true);
        line.setIssueId("issue 2");
        line.addTestCase(testCases.get("test4"));
        lines.add(line);

        line = new Line(1, "file4");
        line.setRelevant(true);
        line.setIssueId("issue 2");
        lines.add(line);

        line = new Line(1, "file5");
        line.setRelevant(true);
        line.setIssueId("issue 3");
        line.addTestCase(testCases.get("test1"));
        lines.add(line);

        line = new Line(2, "file5");
        line.setRelevant(true);
        line.setIssueId("issue 4");
        line.addTestCase(testCases.get("test4"));
        lines.add(line);

        line = new Line(1, "file6");
        line.setRelevant(true);
        line.setIssueId("issue 5");
        line.addTestCase(testCases.get("test4"));
        lines.add(line);

        line = new Line(2, "file6");
        line.setRelevant(false);
        line.setIssueId("issue 6");
        line.addTestCase(testCases.get("test4"));
        lines.add(line);

        statisticsCalculator = new StatisticsCalculator(persistenceEntity);
    }

    @Test
    public void testGetNumberOfPositiveTests() throws Exception {
        assertEquals(3, statisticsCalculator.getNumberOfPositiveTests());
    }

    @Test
    public void testRelevantLines() throws Exception {
        assertEquals(9, statisticsCalculator.countRelevantLines());
    }

    @Test
    public void testCountCoveredLines() throws Exception {
        assertEquals(7, statisticsCalculator.countCoveredLines());
    }

    @Test
    public void testCountPositivelyCoveredLines() throws Exception {
        assertEquals(3, statisticsCalculator.countPositivelyCoveredLines());
    }

    @Test
    public void testCountRelevantLinesForIssue() throws Exception {
        assertEquals(3, statisticsCalculator.countRelevantLines("issue 1"));
        assertEquals(3, statisticsCalculator.countRelevantLines("issue 2"));
        assertEquals(1, statisticsCalculator.countRelevantLines("issue 3"));
        assertEquals(1, statisticsCalculator.countRelevantLines("issue 4"));
        assertEquals(1, statisticsCalculator.countRelevantLines("issue 5"));
        assertEquals(0, statisticsCalculator.countRelevantLines("issue 6"));
    }

    @Test
    public void testCountCoveredLinesForIssue() throws Exception {
        assertEquals(2, statisticsCalculator.countCoveredLines("issue 1"));
        assertEquals(2, statisticsCalculator.countCoveredLines("issue 2"));
        assertEquals(1, statisticsCalculator.countCoveredLines("issue 3"));
        assertEquals(1, statisticsCalculator.countCoveredLines("issue 4"));
        assertEquals(1, statisticsCalculator.countCoveredLines("issue 5"));
        assertEquals(0, statisticsCalculator.countCoveredLines("issue 6"));
    }

    @Test
    public void testCountPositivelyCoveredLinesForIssue() throws Exception {
        assertEquals(1, statisticsCalculator.countPositivelyCoveredLines("issue 1"));
        assertEquals(1, statisticsCalculator.countPositivelyCoveredLines("issue 2"));
        assertEquals(1, statisticsCalculator.countPositivelyCoveredLines("issue 3"));
        assertEquals(0, statisticsCalculator.countPositivelyCoveredLines("issue 4"));
        assertEquals(0, statisticsCalculator.countPositivelyCoveredLines("issue 5"));
        assertEquals(0, statisticsCalculator.countPositivelyCoveredLines("issue 6"));
    }

    @Test
    public void testNumberOfTestCasesForIssue() throws Exception {
        assertEquals(2, statisticsCalculator.getTestCasesForIssue("issue 1").size());
        assertEquals(2, statisticsCalculator.getTestCasesForIssue("issue 2").size());
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 3").size());
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 4").size());
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 5").size());
        assertEquals(0, statisticsCalculator.getTestCasesForIssue("issue 6").size());
    }
}