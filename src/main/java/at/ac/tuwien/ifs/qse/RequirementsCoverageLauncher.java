package at.ac.tuwien.ifs.qse;

import at.ac.tuwien.ifs.qse.coverageReportParser.CoverageAnalyser;
import at.ac.tuwien.ifs.qse.coverageReportParser.JaCoCo;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import at.ac.tuwien.ifs.qse.reportGenerator.ReportGenerator;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.GitRepositoryAnalyser;
import at.ac.tuwien.ifs.qse.repositoryAnalyser.RepositoryAnalyser;
import at.ac.tuwien.ifs.qse.requirementsParser.JiraRequirementsParser;
import at.ac.tuwien.ifs.qse.service.RemoteMavenRunner;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RequirementsCoverageLauncher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequirementsCoverageLauncher.class);


    public static void main( String[] args ) throws IOException {
        long startTime = System.currentTimeMillis();

        // parse arguments ------------------------------
        if (args.length != 4) {
            LOGGER.error("wrong number of arguments. required arguments: " +
                    "targetRepositoryPath targetProjectPath requirementsPath issueIdRegEx");
            return;
        }
        String targetRepositoryPath = args[0];
        String targetProjectPath = args[1];
        String requirementsPath = args[2];
        String issueIdRegEx = args[3];
        Persistence persistence = new PersistenceEntity(
                targetRepositoryPath,
                targetProjectPath,
                issueIdRegEx,
                requirementsPath
        );
        // -----------------------------------------------

        // clean project ---------------------------------
        try {
            RemoteMavenRunner.runRemoteMaven(targetProjectPath + "/pom.xml",
                    Arrays.asList("clean", "-q"));
        } catch (MavenInvocationException e) {
            LOGGER.error("an error occurred while executing mvn clean: ", e);
            return;
        }
        // -----------------------------------------------

        // parse repository ------------------------------
        Repository repository = new FileRepositoryBuilder()
                .setMustExist(true)
                .setGitDir(new File(targetRepositoryPath + "/.git"))
                .build();

        RepositoryAnalyser gitRepositoryAnalyser = new GitRepositoryAnalyser(persistence, repository);
        gitRepositoryAnalyser.analyseRepository();

        long repoEndTime = System.currentTimeMillis();
        LOGGER.info("repository parsed, elapsed time: " + ((repoEndTime - startTime)/ 1000d) + " sec.");
        // -----------------------------------------------

        // parse Requirements ----------------------------
        LOGGER.info("parsing requirements...");
        JiraRequirementsParser jiraRequirementsParser = new JiraRequirementsParser(persistence);
        try {
            jiraRequirementsParser.parseRequirements();
        } catch (Exception e) {
            LOGGER.error("an error occurred while parsing requirements: ", e);
            persistence.setShowWarning(true);
        }
        LOGGER.info(persistence.toString());
        long requirementsEndTime = System.currentTimeMillis();
        LOGGER.info("requirements parsed, elapsed time: " + ((requirementsEndTime - repoEndTime)/ 1000d) + " sec.");
        // -----------------------------------------------

        // parse test and coverage reports ---------------
        CoverageAnalyser coverageAnalyser = new CoverageAnalyser(persistence, new JaCoCo(persistence));
        coverageAnalyser.analyzeCoverage();

        long parsingEndTime = System.currentTimeMillis();
        LOGGER.info("test and coverage reports parsed, elapsed time " + ((parsingEndTime - requirementsEndTime )/ 60000d) + " min.");
        // -----------------------------------------------

        // generate report -------------------------------
        ReportGenerator reportGenerator = new ReportGenerator(persistence);
        reportGenerator.generateReport();

        long statisticsEndTime = System.currentTimeMillis();
        LOGGER.info("requirements coverage report generated, elapsed time: " + ((statisticsEndTime - parsingEndTime)/ 1000d) + " sec.");
        // -----------------------------------------------

        LOGGER.info("total elapsed time: " + ((statisticsEndTime - startTime)/ 60000d) + " min.");

    }
}
