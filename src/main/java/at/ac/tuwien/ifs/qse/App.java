package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.ModelAccessService;

public class App 
{
    public static void main( String[] args )
    {
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(new JaCoCo());
        coverageAnalyser.analyzeCoverage();
        
        //Print Test report
        for (TestCase testCase :
                ModelAccessService.getTestCases().values()) {
            System.out.println(testCase.getTestCaseName() + " is positive: " + testCase.isPositive());
        }
    }
}
