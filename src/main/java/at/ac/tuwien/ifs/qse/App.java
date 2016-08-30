package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.ModelAccessService;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException {
        String path = args[0]+ " " + args[1];
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(new JaCoCo());
        coverageAnalyser.analyzeCoverage(path);
        int count = 0;
        int positive = 0;

        //Print Test report
        for (TestCase testCase :
                ModelAccessService.getTestCases().values()) {
            System.out.println(testCase.getTestCaseName() + " is positive: " + testCase.isPositive());
            count ++;
            if (testCase.isPositive())
                positive ++;
        }
        System.out.println("ratio of success: " + 100 * positive/((double) count) + "%");
    }
}
