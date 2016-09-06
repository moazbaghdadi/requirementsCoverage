package at.ac.tuwien.ifs.qse.reportCreator;

import at.ac.tuwien.ifs.qse.service.PersistenceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple report printer.
 * //TODO: add html report generator
 */
public class ReportPrinter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportPrinter.class);
    private PersistenceEntity persistenceEntity;

    public ReportPrinter(PersistenceEntity persistenceEntity) {
        this.persistenceEntity = persistenceEntity;
    }

    public void printOutStatistics(){
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator(persistenceEntity);
        LOGGER.info("printing report");

        System.out.println(("--------------------------------------------------"));
        System.out.println(("------------------   Report   --------------------"));
        System.out.println(("--------------------------------------------------"));

        System.out.println(("the repository contains " + persistenceEntity.getFiles().size() + " files."));
        System.out.println(("the repository contains " + persistenceEntity.getIssues().size() + " issues."));
        System.out.println(("the repository contains " + persistenceEntity.getLines().size() + " lines:"));
        System.out.println(("\t* relevant lines: " + statisticsCalculator.countRelevantLines() + " lines."));
        System.out.println(("\t* covered lines: " + statisticsCalculator.countCoveredLines() + " lines."));
        System.out.println(("\t* positively covered lines: " + statisticsCalculator.countPositivelyCoveredLines() + " lines."));
        System.out.println(("the repository contains " + persistenceEntity.getTestCases().size() + " test cases."));
        System.out.println(("the repository contains " + statisticsCalculator.getNumberOfPositiveTests() + " positive test cases."));

        System.out.println(("--------------------------------------------------"));
        System.out.println(("--------------------------------------------------"));
        LOGGER.info("printing issues report...");
        for (String issueId :
                persistenceEntity.getIssues().keySet()) {
            System.out.println(("--------------------------------------------------"));
            System.out.println(("Issue " + issueId + " has:"));
            System.out.println(("\trelevant lines: " + statisticsCalculator.countRelevantLines(issueId)));
            System.out.println(("\tcovered lines: " + statisticsCalculator.countCoveredLines(issueId)));
            System.out.println(("\tpositively covered lines: " + statisticsCalculator.countPositivelyCoveredLines(issueId)));
        }
        System.out.println(("--------------------------------------------------"));
    }

}
