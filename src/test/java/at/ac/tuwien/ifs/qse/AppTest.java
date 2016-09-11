package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.reportGenerator.StatisticsCalculatorTest;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.GitRepositoryAnalyserTest;
import at.ac.tuwien.ifs.qse.service.JaCoCoSAXHandlerTest;
import at.ac.tuwien.ifs.qse.service.TestReportSAXHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Unit test for simple App.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        StatisticsCalculatorTest.class,
        GitRepositoryAnalyserTest.class,
        JaCoCoSAXHandlerTest.class,
        TestReportSAXHandlerTest.class
})
public class AppTest {

}
