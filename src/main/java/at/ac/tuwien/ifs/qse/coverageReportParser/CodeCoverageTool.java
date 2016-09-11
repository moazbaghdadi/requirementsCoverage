package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;

interface CodeCoverageTool {

    /**
     * Generates a code coverage report for all test cases and finds
     * the relevant lines of code from it.
     *
     * @throws Exception if an error occurred.
     */
    void analyseRelevantLines() throws Exception;

    /**
     * Generates a code coverage report for a test case and analyses
     * it.
     *
     * @param testCase the test case to generate the report for.
     * @throws Exception if an error occurred.
     */
    void analyseCoverageReport(TestCase testCase) throws Exception;
}
