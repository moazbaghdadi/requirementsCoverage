package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class StatisticsCalculatorTest.
 */
public class StatisticsCalculatorTest{

    private StatisticsCalculator statisticsCalculator;

    @Before
    public void setUp() throws Exception {
        PersistenceEntity persistenceEntity = new PersistenceEntity(null, null, null);

        Issue issue1 = new Issue("issue 1");
        Issue issue2 = new Issue("issue 2");
        Issue issue3 = new Issue("issue 3");
        Issue issue4 = new Issue("issue 4");
        Issue issue5 = new Issue("issue 5");
        Issue issue6 = new Issue("issue 6");
        persistenceEntity.addIssue(issue1);
        persistenceEntity.addIssue(issue2);
        persistenceEntity.addIssue(issue3);
        persistenceEntity.addIssue(issue4);
        persistenceEntity.addIssue(issue5);
        persistenceEntity.addIssue(issue6);

        TestCase testCase1 = new TestCase("test1", true);
        TestCase testCase2 = new TestCase("test2", true);
        TestCase testCase3 = new TestCase("test3", true);

        TestCase testCase4 = new TestCase("test4", false);
        TestCase testCase5 = new TestCase("test5", false);
        TestCase testCase6 = new TestCase("test6", false);

        persistenceEntity.addTestCase(testCase1);
        persistenceEntity.addTestCase(testCase2);
        persistenceEntity.addTestCase(testCase3);
        persistenceEntity.addTestCase(testCase4);
        persistenceEntity.addTestCase(testCase5);
        persistenceEntity.addTestCase(testCase6);

        File file1 = new File("file1");
        File file2 = new File("file2");
        File file3 = new File("file3");
        File file4 = new File("file4");
        File file5 = new File("file5");
        File file6 = new File("file6");
        File file7 = new File("file7");

        persistenceEntity.addFile(file1);
        persistenceEntity.addFile(file2);
        persistenceEntity.addFile(file3);
        persistenceEntity.addFile(file4);
        persistenceEntity.addFile(file5);
        persistenceEntity.addFile(file6);
        persistenceEntity.addFile(file7);


        Line line1 = new Line(1, "file1");
        line1.setRelevant(true);
        line1.setIssueId("issue 1");
        line1.addTestCase(testCase1);
        persistenceEntity.addLine(line1);

        Line line2 = new Line(2, "file1");
        line2.setRelevant(true);
        line2.setIssueId("issue 1");
        persistenceEntity.addLine(line2);

        Line line3 = new Line(3, "file1");
        line3.setRelevant(true);
        line3.setIssueId("issue 1");
        line3.addTestCase(testCase4);
        persistenceEntity.addLine(line3);

        Line line4 = new Line(1, "file2");
        line4.setRelevant(true);
        line4.setIssueId("issue 2");
        line4.addTestCase(testCase1);
        persistenceEntity.addLine(line4);

        Line line5 = new Line(1, "file3");
        line5.setRelevant(true);
        line5.setIssueId("issue 2");
        line5.addTestCase(testCase4);
        persistenceEntity.addLine(line5);

        Line line6 = new Line(1, "file4");
        line6.setRelevant(true);
        line6.setIssueId("issue 2");
        persistenceEntity.addLine(line6);

        Line line7 = new Line(1, "file5");
        line7.setRelevant(true);
        line7.setIssueId("issue 3");
        line7.addTestCase(testCase1);
        persistenceEntity.addLine(line7);

        Line line8 = new Line(2, "file5");
        line8.setRelevant(true);
        line8.setIssueId("issue 4");
        line8.addTestCase(testCase4);
        persistenceEntity.addLine(line8);

        Line line9 = new Line(1, "file6");
        line9.setRelevant(true);
        line9.setIssueId("issue 5");
        line9.addTestCase(testCase4);
        persistenceEntity.addLine(line9);

        Line line10 = new Line(2, "file6");
        line10.setRelevant(false);
        line10.setIssueId("issue 6");
        line10.addTestCase(testCase4);
        persistenceEntity.addLine(line10);

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