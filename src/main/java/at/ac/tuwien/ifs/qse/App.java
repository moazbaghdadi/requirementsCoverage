package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.PersistenceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
        private static long startTime = System.currentTimeMillis();

    public static void main( String[] args ) throws IOException {
        String targetProjectPath = args[0]+ " " + args[1];
        PersistenceEntity persistenceEntity = new PersistenceEntity(targetProjectPath);

        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(persistenceEntity, new JaCoCo(persistenceEntity));
        coverageAnalyser.analyzeCoverage();
        int count = 0;
        int positive = 0;

        //Print Test report
        for (TestCase testCase :
                persistenceEntity.getTestCases().values()) {
            count ++;
            if (testCase.isPositive())
                positive ++;
        }
        LOGGER.info("positive test cases are " + 100 * positive/((double) count) + "% of all test cases");

        count = 0;
        for (Line line :
                persistenceEntity.getLines()) {
            count++;
        }
        LOGGER.info("number of covered lines: " + count);

        long endTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((endTime - startTime)/ 60000) + " minutes");
    }
}
