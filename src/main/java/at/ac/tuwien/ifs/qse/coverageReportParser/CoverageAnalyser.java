package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;

import java.util.List;

/**
 * Generates the test report and parses it's information.
 * Generates for each test case a code coverage report and
 * parses it's information.
 *
 */
public class CoverageAnalyser {
    private List<TestCase> testcases;
    public void run(){
        generateTestReport();
        analyseTestReport();

        for (TestCase testCase : testcases) {
            generateCoverageReport(testCase);
            analyseCoverageReport();
        }

    }

    private void generateTestReport(){
        //TODO implement method
    }

    private void analyseTestReport(){
        //TODO implement method
    }

    private void generateCoverageReport(TestCase TestCaseName){
        //TODO implement method
    }

    private void analyseCoverageReport(){
        //TODO implement method
    }

}
