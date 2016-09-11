package at.ac.tuwien.ifs.qse.persistence;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides singleton lists to store information in.
 */
public class PersistenceEntity implements Persistence {

    private Set<File> files;
    private Set<Issue> issues;
    private Set<TestCase> testCases;
    private Set<Line> lines;
    private String targetRepositoryPath;
    private String targetProjectPath;
    private String issueIdsRegEx;

    public PersistenceEntity(String targetRepositoryPath, String targetProjectPath, String commitsRegEx) {
        this.targetRepositoryPath = targetRepositoryPath;
        this.targetProjectPath = targetProjectPath;
        this.issueIdsRegEx = commitsRegEx;

        files = new HashSet<>();
        issues = new HashSet<>();
        testCases = new HashSet<>();
        lines = new HashSet<>();
    }

    @Override
    public void addFile(File file) {
        if(files.contains(file)) {
            files.remove(file);
        }
        files.add(file);
    }

    @Override
    public File getFile(String fileName) {
        return files.stream()
                .filter(file -> file.getFileName().equals(fileName))
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<File> getFiles(){
        return new HashSet<>(files);
    }

    @Override
    public void addIssue(Issue issue) {
        if (issues.contains(issue)) {
            issues.remove(issue);
        }
        issues.add(issue);
    }

    @Override
    public Issue getIssue(String issueId) {
        return issues.stream()
                .filter(issue -> issue.getIssueId().equals(issueId))
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<Issue> getIssues(){
        return new HashSet<>(issues);
    }

    @Override
    public void addTestCase(TestCase testCase) {
        if (testCases.contains(testCase)) {
            testCases.remove(testCase);
        }
        testCases.add(testCase);
    }

    @Override
    public TestCase getTestCase(String testCaseName) {
        return testCases.stream()
                .filter(testCase -> testCase.getTestCaseName().equals(testCaseName))
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<TestCase> getTestCases() {
        return new HashSet<>(testCases);
    }

    @Override
    public void addLine(Line line) {
        if (!lines.add(line)) {
            lines.remove(line);
            lines.add(line);
        }
    }

    @Override
    public Line getLine(int lineNumber, String fileName) {
        return lines.stream()
                .filter(line -> line.getFileName().contains(fileName)
                        && line.getLineNumber() == lineNumber)
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<Line> getLines(){
        return new HashSet<>(lines);
    }

    @Override
    public void setTargetRepositoryPath(String targetRepositoryPath) {
        this.targetRepositoryPath = targetRepositoryPath;
    }

    @Override
    public String getTargetRepositoryPath() {
        return targetRepositoryPath;
    }

    @Override
    public void setTargetProjectPath(String targetProjectPath) {
        this.targetProjectPath = targetProjectPath;
    }

    @Override
    public String getTargetProjectPath(){
        return targetProjectPath;
    }

    @Override
    public void setIssueIdsRegEx(String issueIdsRegEx) {
        this.issueIdsRegEx  = issueIdsRegEx;
    }

    @Override
    public String getIssueIdsRegEx() {
        return issueIdsRegEx;
    }

    @Override
    public String toString() {
        return "PersistenceEntity{" +
                "files=" + files.size() +
                ", issues=" + issues.size() +
                ", testCases=" + testCases.size() +
                ", lines=" + lines.size() +
                ", relevantLines=" + lines.stream().filter(Line::isRelevant).count() +
                '}';
    }
}
