package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitRepositoryAnalyser implements RepositoryAnalyser {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepositoryAnalyser.class);
    private Persistence persistence;
    private Git git;

    public GitRepositoryAnalyser(Persistence persistence, Repository repository) {
        this.persistence = persistence;
        this.git = new Git(repository);
    }

    public void analyseRepository() {
        LOGGER.info("parsing repository...");
        int failuresCounter = 0;
        List<Throwable> exceptions = new ArrayList<>();

        List<String> projectFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(persistence.getTargetProjectPath())).forEach(filePath -> {
                if (Files.isRegularFile(filePath) && filePath.toString().matches(".*java\\b")) {
                    projectFiles.add(filePath.toString().substring(persistence.getTargetRepositoryPath().length()+1).replace("\\", "/"));
                }
            });
        } catch (IOException e) {
            failuresCounter ++;
            exceptions.add(e);
            persistence.setShowWarning(true);
        }
        for (String filePath :
                projectFiles) {
            try {
                getLinesInfo(filePath);
            } catch (GitAPIException | IOException e) {
                failuresCounter ++;
                exceptions.add(e);
                persistence.setShowWarning(true);
            }
        }

        if (failuresCounter != 0) {
            LOGGER.error(failuresCounter + " failure(s) happened while parsing the repository.");
            LOGGER.error("listing failures...");
            exceptions.forEach(throwable -> LOGGER.error(throwable.getMessage(), throwable));
        }
    }

    private void getLinesInfo(String filePath) throws GitAPIException, IOException {
        BlameCommand blameCommand = git.blame();
        blameCommand = blameCommand.setStartCommit(git.getRepository().resolve("HEAD"));
        blameCommand = blameCommand.setFilePath(filePath);
        BlameResult blameResult  = blameCommand.call();

        Line line;

        File file = persistence.getFile(filePath.replace("/", "."));
        if (file == null) {
            file = new File(filePath.replace("/", "."));
        }

        try {
            RawText rawText = blameResult.getResultContents();

            for (int i = 0; i < rawText.size(); i++) {
                line = new Line(i, file.getFileName());

                line.setRevisionNumber(blameResult.getSourceCommit(i).getName());
                line.setIssueId(getIssueId(blameResult.getSourceCommit(i).getName()));
                persistence.addLine(line);
                file.addLine(line);
            }
            persistence.addFile(file);
        } catch (NullPointerException e){
            //nothing to be done, the file doesn't exist in the repository (ex: .git, .idea,...)
        }
    }

    private String getIssueId(String revisionId) throws GitAPIException, IOException {
        String issueId = null;
        Issue issue;

        String commitsRegEx = persistence.getIssueIdsRegEx();
        Pattern pattern = Pattern.compile(".*(" + commitsRegEx + ").*", Pattern.DOTALL);
        Matcher matcher;

        Iterable<RevCommit> log = git.log().
                add(git.getRepository().resolve(revisionId)).call();

        if (log.iterator().hasNext()) {
            RevCommit revCommit = log.iterator().next();
            matcher = pattern.matcher(revCommit.getFullMessage());

            if (matcher.matches()) {
                issueId = matcher.group(1);
                issue = persistence.getIssue(issueId);
                if (issue == null){
                    issue = new Issue(issueId);
                }
                issue.addRevisionId(revisionId);
                persistence.addIssue(issue);
            }
        }
        return issueId;
    }

}
