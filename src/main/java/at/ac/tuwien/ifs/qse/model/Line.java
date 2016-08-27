package at.ac.tuwien.ifs.qse.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of code.
 */
public class Line {

    private int lineNumber;
    private String className;
    private String revisionNumber;
    private List<TestCase> testCases;

    public Line(int lineNumber, String className, String revisionNumber) {
        this.lineNumber = lineNumber;
        this.className = className;
        this.revisionNumber = revisionNumber;
        this.testCases = new ArrayList<TestCase>();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
    }
}
