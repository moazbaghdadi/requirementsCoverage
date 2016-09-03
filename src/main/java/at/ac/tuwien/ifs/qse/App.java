package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
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

public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static long startTime = System.currentTimeMillis();

    public static void main( String[] args ) throws IOException {
        if (args.length < 3) {
            LOGGER.error("missing arguments. required arguments: targetProjectPath commitsRegEx");
        }
        String targetProjectPath = args[0]+ " " + args[1];
        String commitsRegEx = args[2];
        PersistenceEntity persistenceEntity = new PersistenceEntity(targetProjectPath, commitsRegEx);

        // parsing repository
        LOGGER.info("parsing repository...");
        Repository repository = new FileRepository(targetProjectPath + "/.git");
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

        boolean isCovered = true;
        boolean positiveCovered = true;
        int covered = 0;
        positive = 0;
        for (Line line: persistenceEntity.getLines()) {
            if (line.getTestCases().isEmpty()) {
                isCovered = false;
            }
            for (TestCase testCase : line.getTestCases()) {
                if (!testCase.isPositive()){
                    positiveCovered = false;
                    break;
                }
            }
            if (isCovered) {
                covered ++;
                if (positiveCovered) {
                    positive ++;
                }
            }

            positiveCovered = true;
            isCovered = true;
        }
        LOGGER.info("number of covered lines: " + covered);
        LOGGER.info("number of lines covered by positive test cases: " + positive);

        long statisticsEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((statisticsEndTime - parsingEndTime)/ 1000d) + " seconds for the statistics");

        LOGGER.info("It took " + ((statisticsEndTime - startTime)/ 60000d) + " minutes for the whole process");

    }
}
