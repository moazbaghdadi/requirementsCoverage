package at.ac.tuwien.ifs.qse.persistence;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;

import java.util.Set;

/**
 * Provides a persistence unit to all packages to share data
 */
public interface Persistence {

    /**
     * Persists the given file. If the file already exists,
     * it will be replaced with the new file.
     *
     * @param file the file to be added.
     */
    void addFile(File file);

    /**
     * Returns the file with the given file name. If the
     * file doesn't exist, returns null.
     *
     * @param fileName the name of the file to be returned.
     * @return the file with the given file name. If the
     * file doesn't exist, returns null
     */
    File getFile(String fileName);

    /**
     * Returns a set containing all files.
     * @return a set containing all files
     */
    Set<File> getFiles();

    /**
     * Persists the given issue. If the issue already exists,
     * it will be replaced with the new issue.
     *
     * @param issue the issue to be added.
     */
    void addIssue(Issue issue);

    /**
     * Returns the issue with the given issueId. If the
     * issue doesn't exist, returns null.
     *
     * @param issueId the id of the issue to be returned.
     * @return the issue with the given issueId. If the
     * issue doesn't exist, returns null
     */
    Issue getIssue(String issueId);

    /**
     * Returns a set containing all issues.
     *
     * @return a set containing all issues
     */
    Set<Issue> getIssues();

    /**
     * Persists the given test case. If the test case already exists,
     * it will be replaced with the new test case.
     *
     * @param testCase the test case to be added.
     */
    void addTestCase(TestCase testCase);

    /**
     * Returns the test case with the given test case name. If the
     * test case doesn't exist, returns null.
     *
     * @param testCaseName the name of the test case to be returned.
     * @return the test case with the given issueId. If the
     * test case doesn't exist, returns null
     */
    TestCase getTestCase(String testCaseName);

    /**
     * Returns a set containing all test cases.
     *
     * @return a set containing all test cases
     */
    Set<TestCase> getTestCases();

    /**
     * Persists the given line. If the line already exists,
     * it will be replaced with the new line.
     *
     * @param line the line to be added.
     */
    void addLine(Line line);

    /**
     * Returns the line with the given line number and file name. If the
     * line doesn't exist, returns null.
     *
     * @param lineNumber the line number of the line to be returned.
     * @param fileName the file name of the file containing the line.
     * @return the line with the given line number and file name. If the
     * line doesn't exist, returns null
     */
    Line getLine(int lineNumber, String fileName);

    /**
     * Returns a set containing all lines.
     *
     * @return a set containing all lines
     */
    Set<Line> getAllLines();

    /**
     * Persists the given relevant line. If the line already exists,
     * it will be replaced with the new line.
     *
     * @param line the line to be added
     */
    void addRelevantLine(Line line);

    /**
     * Returns the relevant line with the given line number and file name. If the
     * line doesn't exist, returns null.
     *
     * @param lineNumber the line number of the line to be returned.
     * @param fileName the file name of the file containing the line.
     * @return the relevant line with the given line number and file name. If the
     * line doesn't exist, returns null
     */
    Line getRelevantLine(int lineNumber, String fileName);

    /**
     * Returns a set containing all relevant lines.
     *
     * @return a set containing all relevant lines.
     */
    Set<Line> getRelevantLines();

    /**
     * sets the path to the target project's repository.
     *
     * @param targetRepositoryPath the path to the target project's repository.
     */
    void setTargetRepositoryPath(String targetRepositoryPath);

    /**
     * gets the path to the target project's repository.
     *
     * @return the path to the target project's repository
     */
    String getTargetRepositoryPath();

    /**
     * sets the path to the target project. This path is the same of or a sub path of the
     * target repository path.
     *
     * @param targetProjectPath the path to the target project.
     */
    void setTargetProjectPath(String targetProjectPath);

    /**
     * gets the path to the target project.
     *
     * @return the path to the target project
     */
    String getTargetProjectPath();

    /**
     * sets the RegEx that identifies the issue ids in the commit messages of the
     * target project.
     *
     * @param issueIdsRegEx the RegEx that identifies the issue ids in the commit messages of the
     * target project.
     */
    void setIssueIdsRegEx(String issueIdsRegEx);

    /**
     * gets the RegEx that identifies the issue ids in the commit messages of the
     * target project.
     *
     * @return the RegEx that identifies the issue ids in the commit messages of the
     * target project
     */
    String getIssueIdsRegEx();
}
