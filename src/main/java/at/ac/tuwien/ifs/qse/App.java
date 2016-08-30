package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.ModelAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws IOException {
        String path = args[0]+ " " + args[1];
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(new JaCoCo());
        coverageAnalyser.analyzeCoverage(path);
        int count = 0;
        int positive = 0;

        //Print Test report
        for (TestCase testCase :
                ModelAccessService.getTestCases().values()) {
            count ++;
            if (testCase.isPositive())
                positive ++;
        }
        LOGGER.info("positive test cases are " + 100 * positive/((double) count) + "% of all test cases");
    }
}
