package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import at.ac.tuwien.ifs.qse.reportGenerator.ReportGenerator;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.GitRepositoryAnalyser;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.RepositoryAnalyser;
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
        if (args.length < 5) {
            LOGGER.error("missing arguments. required arguments: targetRepositoryPath targetProjectPath issueIdRegEx");
            return;
        }
        String targetRepositoryPath = args[0]+ " " + args[1];
        String targetProjectPath = args[2]+ " " + args[3];
        String issueIdRegEx = args[4];
        Persistence persistence = new PersistenceEntity(targetRepositoryPath, targetProjectPath, issueIdRegEx);

        // parsing repository
        LOGGER.info("parsing repository...");
        Repository repository = new FileRepository(targetRepositoryPath + "/.git");
        RepositoryAnalyser gitRepositoryAnalyser = new GitRepositoryAnalyser(persistence, repository);
        try {
            gitRepositoryAnalyser.analyseRepository();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        long repoEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((repoEndTime - startTime)/ 1000d) + " seconds to parse repository");

        // parsing test and coverage reports
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(persistence, new JaCoCo(persistence));
        coverageAnalyser.analyzeCoverage();

        long parsingEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((parsingEndTime - repoEndTime)/ 60000d) + " minutes to parse test and coverage reports");

        // printReport
        ReportGenerator reportGenerator = new ReportGenerator(persistence);
        reportGenerator.printOutStatistics();

        long statisticsEndTime = System.currentTimeMillis();
        LOGGER.info("It took " + ((statisticsEndTime - parsingEndTime)/ 1000d) + " seconds for the statistics");

        LOGGER.info("It took " + ((statisticsEndTime - startTime)/ 60000d) + " minutes for the whole process");

    }
}
