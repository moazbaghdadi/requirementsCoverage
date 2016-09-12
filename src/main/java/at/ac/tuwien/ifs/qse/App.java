package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import at.ac.tuwien.ifs.qse.reportGenerator.ReportGenerator;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.GitRepositoryAnalyser;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.RepositoryAnalyser;
import at.ac.tuwien.ifs.qse.service.RemoteMavenRunner;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    private static final long startTime = System.currentTimeMillis();

    public static void main( String[] args ) throws IOException {
        if (args.length != 3) {
            LOGGER.error("wrong number of arguments. required arguments: targetRepositoryPath targetProjectPath issueIdRegEx");
            return;
        }
        String targetRepositoryPath = args[0];
        String targetProjectPath = args[1];
        String issueIdRegEx = args[2];
        Persistence persistence = new PersistenceEntity(targetRepositoryPath, targetProjectPath, issueIdRegEx);

        // clean project
        try {
            RemoteMavenRunner.runRemoteMaven(targetProjectPath + "/pom.xml",
                    Arrays.asList("clean", "-q"));
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }

        // parsing repository
        LOGGER.info("parsing repository...");
        Repository repository = new FileRepositoryBuilder()
                .setMustExist(true)
                .setGitDir(new File(targetRepositoryPath + "/.git"))
                .build();

        RepositoryAnalyser gitRepositoryAnalyser = new GitRepositoryAnalyser(persistence, repository);
        try {
            gitRepositoryAnalyser.analyseRepository();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        LOGGER.info(persistence.toString());
        long repoEndTime = System.currentTimeMillis();
        LOGGER.info("repository parsed, elapsed time: " + ((repoEndTime - startTime)/ 1000d) + " sec.");

        // parsing test and coverage reports
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(persistence, new JaCoCo(persistence));
        coverageAnalyser.analyzeCoverage();

        long parsingEndTime = System.currentTimeMillis();
        LOGGER.info("test and coverage reports parsed, elapsed time " + ((parsingEndTime - repoEndTime)/ 60000d) + " min.");

        // printReport
        ReportGenerator reportGenerator = new ReportGenerator(persistence);
        reportGenerator.printOutStatistics();

        long statisticsEndTime = System.currentTimeMillis();
        LOGGER.info("requirements coverage report generated, elapsed time: " + ((statisticsEndTime - parsingEndTime)/ 1000d) + " sec.");

        LOGGER.info("total elapsed time: " + ((statisticsEndTime - startTime)/ 60000d) + " min.");

    }
}
