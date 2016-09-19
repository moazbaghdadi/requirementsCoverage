package at.ac.tuwien.ifs.qse.persistence;

import at.ac.tuwien.ifs.qse.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides singleton lists to store information in.
 */
public class PersistenceEntity implements Persistence {

    private Set<File> files;
    private Set<Issue> issues;
    private Set<Requirement> requirements;
    private Set<TestCase> testCases;
    private Set<Line> allLines;
    private Set<Line> relevantLines;
    private String targetRepositoryPath;
    private String targetProjectPath;
    private String issueIdsRegEx;
    private String requirementsPath;
    private boolean showWarning;

    public PersistenceEntity(String targetRepositoryPath,
                             String targetProjectPath,
                             String commitsRegEx,
                             String requirementsPath) {
        this.targetRepositoryPath = targetRepositoryPath;
        this.targetProjectPath = targetProjectPath;
        this.issueIdsRegEx = commitsRegEx;
        this.requirementsPath = requirementsPath;
        this.showWarning = false;

        files = new HashSet<>();
        issues = new HashSet<>();
        requirements = new HashSet<>();
        testCases = new HashSet<>();
        allLines = new HashSet<>();
        relevantLines = new HashSet<>();
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
        if (allLines.contains(line)) {
            allLines.remove(line);
        }
        allLines.add(line);
    }

    @Override
    public Line getLine(int lineNumber, String fileName) {
        return allLines.stream()
                .filter(line -> line.getFileName().contains(fileName)
                        && line.getLineNumber() == lineNumber)
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<Line> getAllLines(){
        return new HashSet<>(allLines);
    }

    @Override
    public void addRelevantLine(Line line) {
        if (relevantLines.contains(line)) {
            relevantLines.remove(line);
        }
        relevantLines.add(line);
    }

    @Override
    public Line getRelevantLine(int lineNumber, String fileName) {
        return relevantLines.stream()
                .filter(line -> line.getFileName().contains(fileName)
                        && line.getLineNumber() == lineNumber)
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<Line> getRelevantLines() {
        return new HashSet<>(relevantLines);
    }

    @Override
    public void addRequirement(Requirement requirement) {
        if (requirements.contains(requirement)) {
            requirements.remove(requirement);
        }
        requirements.add(requirement);
    }

    @Override
    public Requirement getRequirement(String requirementId) {
        return requirements.stream()
                .filter(requirement -> requirement.getRequirementId().equals(requirementId))
                .findAny()
                .orElse(null);
    }

    @Override
    public Set<Requirement> getRequirements() {
        return new HashSet<>(requirements);
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
    public void setRequirementsPath(String requirementsPath) {
        this.requirementsPath = requirementsPath;
    }

    @Override
    public String getRequirementsPath() {
        return requirementsPath;
    }

    @Override
    public void setShowWarning(boolean showWarning) {
        this.showWarning = showWarning;
    }

    @Override
    public boolean showWarning() {
        return showWarning;
    }

    @Override
    public String toString() {
        return "PersistenceEntity{" +
                "files=" + files.size() +
                ", issues=" + issues.size() +
                ", requirements" + requirements.size() +
                ", testCases=" + testCases.size() +
                ", allLines=" + allLines.size() +
                ", relevantLines=" + relevantLines.size() +
                '}';
    }
}
