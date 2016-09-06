package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.GitRepositoryAnalyser;
import at.ac.tuwien.ifs.qse.service.PersistenceEntity;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static long startTime = System.currentTimeMillis();

    public static void main( String[] args ) throws IOException {
        if (args.length < 5) {
            LOGGER.error("missing arguments. required arguments: targetProjectPath commitsRegEx");
        }
        String targetRepositoryPath = args[0]+ " " + args[1];
        String targetProjectPath = args[2]+ " " + args[3];
        String commitsRegEx = args[4];
        PersistenceEntity persistenceEntity = new PersistenceEntity(targetRepositoryPath, targetProjectPath, commitsRegEx);

        // parsing repository
        LOGGER.info("parsing repository...");
        Repository repository = new FileRepository(targetRepositoryPath + "/.git");
        GitRepositoryAnalyser gitRepositoryAnalyser = new GitRepositoryAnalyser(persistenceEntity, repository);
        try {
            gitRepositoryAnalyser.analyseRepository();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        long repoEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((repoEndTime - startTime)/ 1000d) + " seconds to parse repository");

        // parsing test and coverage reports
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(persistenceEntity, new JaCoCo(persistenceEntity));
        coverageAnalyser.analyzeCoverage();

        long parsingEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((parsingEndTime - repoEndTime)/ 60000d) + " minutes to parse test and coverage reports");

        // statistics
        LOGGER.info("the repository contains " + persistenceEntity.getLines().size() + " lines.");
        LOGGER.info("the repository contains " + persistenceEntity.getFiles().size() + " files.");
        LOGGER.info("the repository contains " + persistenceEntity.getIssues().size() + " issues.");
        LOGGER.info("the repository contains " + persistenceEntity.getTestCases().size() + " test cases.");
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

        Map<String, Issue> issues = persistenceEntity.getIssues();
        String issueId;
        boolean isCovered = true;
        boolean positiveCovered = true;
        int covered = 0;
        int relevant = 0;
        positive = 0;
        for (Line line: persistenceEntity.getLines()) {
            if(line.isRelevant()) {
                relevant ++;
                if ((issueId = line.getIssueId()) != null) {
                    issues.get(issueId).incrementLines();
                }
                if (line.getTestCases().isEmpty()) {
                    isCovered = false;
                }
                if (line.getTestCases().stream()
                        .filter(testCase -> !testCase.isPositive())
                        .findAny()
                        .orElse(null) == null) {
                    positiveCovered = false;
                }
                if (isCovered) {
                    if ((issueId = line.getIssueId()) != null) {
                        issues.get(issueId).incrementCoveredLines();
                    }
                    covered++;
                    if (positiveCovered) {
                        if ((issueId = line.getIssueId()) != null) {
                            issues.get(issueId).incrementPositiveCoveredLines();
                        }
                        positive++;
                    }
                }

                positiveCovered = true;
                isCovered = true;
            }
        }
        LOGGER.info("number of relevant lines: " + relevant);
        LOGGER.info("number of covered lines: " + covered);
        LOGGER.info("number of lines covered by positive test cases: " + positive);

        for (Issue issue :
                issues.values()) {
            LOGGER.info("Issue " + issue.getIssueId() + " has:");
            LOGGER.info(issue.getLines() + " lines.");
            LOGGER.info(100 * issue.getCoveredLines()/(double)issue.getLines() + "% of lines are covered.");
            LOGGER.info(100 * issue.getPositiveCoveredLines()/(double)issue.getLines()
                    + "% of lines are covered with positive test cases.");
        }


        long statisticsEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((statisticsEndTime - parsingEndTime)/ 1000d) + " seconds for the statistics");

        LOGGER.info("It took " + ((statisticsEndTime - startTime)/ 60000d) + " minutes for the whole process");

    }
}
