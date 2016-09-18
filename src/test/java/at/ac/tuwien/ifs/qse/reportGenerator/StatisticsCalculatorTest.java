package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.*;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class StatisticsCalculatorTest.
 */
public class StatisticsCalculatorTest{

    private StatisticsCalculator statisticsCalculator;
    private Requirement requirement1;
    private Requirement requirement2;
    private Requirement requirement3;

    @Before
    public void setUp() throws Exception {

        PersistenceEntity persistenceEntity = new PersistenceEntity(null, null, null, null);

        /*
            Model:
            requirement1 {
                issue 1 {
                    Line 1  <---> Relevant <---> TestCase1 {positive}
                    Line 2  <---> Relevant <---> --------------------
                    Line 3  <---> Relevant <---> TestCase4 {negative}
                }
                issue 2 {
                    Line 4  <---> Relevant <---> TestCase1 {positive}
                    Line 5  <---> -------- <---> TestCase4 {negative}
                    Line 6  <---> Relevant <---> --------------------
                }
                issue 3 {
                    Line 7  <---> Relevant <---> TestCase1 {positive}
                }
            }
            requirement 2 {
                issue 4 {
                    Line 8  <---> Relevant <---> TestCase4 {negative}
                }
                issue 5 {
                    Line 9  <---> Relevant <---> TestCase4 {negative}
                }
            }
            requirement 3 {
            }
            no requirement {
                issue 6 {
                        Line 10 <---> -------- <---> TestCase4 {negative}
                }
            }

         */
        Issue issue1 = new Issue("issue 1");
        Issue issue2 = new Issue("issue 2");
        Issue issue3 = new Issue("issue 3");
        Issue issue4 = new Issue("issue 4");
        Issue issue5 = new Issue("issue 5");
        Issue issue6 = new Issue("issue 6");
        requirement1 = new Requirement("requirement1");
        requirement2 = new Requirement("requirement2");
        requirement3 = new Requirement("requirement3");

        requirement1.addIssue(issue1);
        requirement1.addIssue(issue2);
        requirement1.addIssue(issue3);
        issue1.setRequirement(requirement1);
        issue2.setRequirement(requirement1);
        issue3.setRequirement(requirement1);

        requirement2.addIssue(issue4);
        requirement2.addIssue(issue5);
        issue4.setRequirement(requirement2);
        issue5.setRequirement(requirement2);

        persistenceEntity.addIssue(issue1);
        persistenceEntity.addIssue(issue2);
        persistenceEntity.addIssue(issue3);
        persistenceEntity.addIssue(issue4);
        persistenceEntity.addIssue(issue5);
        persistenceEntity.addIssue(issue6);

        persistenceEntity.addRequirement(requirement1);
        persistenceEntity.addRequirement(requirement2);
        persistenceEntity.addRequirement(requirement3);

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
        line1.setIssueId("issue 1");
        line1.addTestCase(testCase1);
        persistenceEntity.addRelevantLine(line1);
        persistenceEntity.addLine(line1);

        Line line2 = new Line(2, "file1");
        line2.setIssueId("issue 1");
        persistenceEntity.addRelevantLine(line2);
        persistenceEntity.addLine(line2);

        Line line3 = new Line(3, "file1");
        line3.setIssueId("issue 1");
        line3.addTestCase(testCase4);
        persistenceEntity.addRelevantLine(line3);
        persistenceEntity.addLine(line3);

        Line line4 = new Line(1, "file2");
        line4.setIssueId("issue 2");
        line4.addTestCase(testCase1);
        persistenceEntity.addRelevantLine(line4);
        persistenceEntity.addLine(line4);

        Line line5 = new Line(1, "file3");
        line5.setIssueId("issue 2");
        line5.addTestCase(testCase4);
        persistenceEntity.addLine(line5);

        Line line6 = new Line(1, "file4");
        line6.setIssueId("issue 2");
        persistenceEntity.addRelevantLine(line6);
        persistenceEntity.addLine(line6);

        Line line7 = new Line(1, "file5");
        line7.setIssueId("issue 3");
        line7.addTestCase(testCase1);
        persistenceEntity.addRelevantLine(line7);
        persistenceEntity.addLine(line7);

        Line line8 = new Line(2, "file5");
        line8.setIssueId("issue 4");
        line8.addTestCase(testCase4);
        persistenceEntity.addRelevantLine(line8);
        persistenceEntity.addLine(line8);

        Line line9 = new Line(1, "file6");
        line9.setIssueId("issue 5");
        line9.addTestCase(testCase4);
        persistenceEntity.addRelevantLine(line9);
        persistenceEntity.addLine(line9);

        Line line10 = new Line(2, "file6");
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
        assertEquals(8, statisticsCalculator.countRelevantLines());
    }

    @Test
    public void testCountCoveredLines() throws Exception {
        assertEquals(6, statisticsCalculator.countCoveredLines());
    }

    @Test
    public void testCountPositivelyCoveredLines() throws Exception {
        assertEquals(3, statisticsCalculator.countPositivelyCoveredLines());
    }

    @Test
    public void testCountRelevantLinesForIssue() throws Exception {
        assertEquals(3, statisticsCalculator.countRelevantLines("issue 1"));
        assertEquals(2, statisticsCalculator.countRelevantLines("issue 2"));
        assertEquals(1, statisticsCalculator.countRelevantLines("issue 3"));
        assertEquals(1, statisticsCalculator.countRelevantLines("issue 4"));
        assertEquals(1, statisticsCalculator.countRelevantLines("issue 5"));
        assertEquals(0, statisticsCalculator.countRelevantLines("issue 6"));
    }

    @Test
    public void testCountCoveredLinesForIssue() throws Exception {
        assertEquals(2, statisticsCalculator.countCoveredLines("issue 1"));
        assertEquals(1, statisticsCalculator.countCoveredLines("issue 2"));
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
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 2").size());
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 3").size());
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 4").size());
        assertEquals(1, statisticsCalculator.getTestCasesForIssue("issue 5").size());
        assertEquals(0, statisticsCalculator.getTestCasesForIssue("issue 6").size());
    }

    @Test
    public void testCountRelevantLinesForRequirement() throws Exception {
        assertEquals(6, statisticsCalculator.countRelevantLines(requirement1));
        assertEquals(2, statisticsCalculator.countRelevantLines(requirement2));
        assertEquals(0, statisticsCalculator.countRelevantLines(requirement3));
    }

    @Test
    public void testCountCoveredLinesForRequirement() throws Exception {
        assertEquals(4, statisticsCalculator.countCoveredLines(requirement1));
        assertEquals(2, statisticsCalculator.countCoveredLines(requirement2));
        assertEquals(0, statisticsCalculator.countCoveredLines(requirement3));
    }

    @Test
    public void testCountPositivelyCoveredLinesForRequirement() throws Exception {
        assertEquals(3, statisticsCalculator.countPositivelyCoveredLines(requirement1));
        assertEquals(0, statisticsCalculator.countPositivelyCoveredLines(requirement2));
        assertEquals(0, statisticsCalculator.countPositivelyCoveredLines(requirement3));
    }

    @Test
    public void testCountLinesForRequirement() throws Exception {
        assertEquals(7, statisticsCalculator.countLines(requirement1));
        assertEquals(2, statisticsCalculator.countLines(requirement2));
        assertEquals(0, statisticsCalculator.countLines(requirement3));
    }
}