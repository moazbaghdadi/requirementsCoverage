package at.ac.tuwien.ifs.qse.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a line of code.
 */
public class Line {

    private int lineNumber;
    private String fileName;
    private String issueId;
    private Set<TestCase> testCases;

    public Line(int lineNumber, String fileName) {
        this.lineNumber = lineNumber;
        this.fileName = fileName;
        this.testCases = new HashSet<>();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<TestCase> getTestCases() {
        return testCases;
    }

    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (lineNumber != line.lineNumber) return false;
        return fileName.contains(line.fileName) || line.fileName.contains(fileName);

    }

    @Override
    public int hashCode() {
        int result = lineNumber;
        result = 31 * result + fileName.hashCode();
        return result;
    }
}
