package at.ac.tuwien.ifs.qse.coverageReportParser;

import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.ModelAccessService;

import java.util.Map;

/**
 * Generates for each test case a code coverage report and
 * parses it's information.
 *
 */
public class CoverageAnalyser {
    private CodeCoverageTool codeCoverageTool;
    private Map<String, TestCase> testCases;

    public CoverageAnalyser(CodeCoverageTool codeCoverageTool) {
        this.codeCoverageTool = codeCoverageTool;
        this.testCases = ModelAccessService.getTestCases();
    }

    public void analyzeCoverage(){
        analyseTestReport();
        for (TestCase testCase : testCases.values()) {
            codeCoverageTool.analyseCoverageReport(testCase);
        }
    }

    /**
     * Generates the test report of the project and analyses it.
     */
    private void analyseTestReport(){
        //TODO implement method
    }

}
