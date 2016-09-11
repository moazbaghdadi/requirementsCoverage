package at.ac.tuwien.ifs.qse.model;

/**
 * Represents a Test Case.
 */
public class TestCase {

    private String testCaseName;
    private boolean positive;

    public TestCase(String testCaseName, boolean positive) {
        this.testCaseName = testCaseName;
        this.positive = positive;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestCase testCase = (TestCase) o;

        return testCaseName.equals(testCase.testCaseName);

    }

    @Override
    public int hashCode() {
        return testCaseName.hashCode();
    }
}
