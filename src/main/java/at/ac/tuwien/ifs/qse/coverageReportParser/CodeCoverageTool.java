package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;

interface CodeCoverageTool {

    /**
     * Generates a code coverage report for a test case and analyses
     * it.
     * @param testCase the test case to generate the report for.
     */
    void analyseCoverageReport(TestCase testCase);
}
