package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.reportGenerator.StatisticsCalculatorTest;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.GitRepositoryAnalyserTest;
import at.ac.tuwien.ifs.qse.xmlParser.JaCoCoSAXHandlerTest;
import at.ac.tuwien.ifs.qse.xmlParser.RequirementsSAXHandlerTest;
import at.ac.tuwien.ifs.qse.xmlParser.TestReportSAXHandlerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Unit test for simple RequirementsCoverageLauncher.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        StatisticsCalculatorTest.class,
        GitRepositoryAnalyserTest.class,
        JaCoCoSAXHandlerTest.class,
        RequirementsSAXHandlerTest.class,
        TestReportSAXHandlerTest.class
})
public class RequirementsCoverageLauncherTest {

}
