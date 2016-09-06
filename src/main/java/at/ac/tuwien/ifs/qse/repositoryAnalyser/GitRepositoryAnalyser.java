package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.service.PersistenceEntity;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitRepositoryAnalyser implements RepositoryAnalyser {
    private PersistenceEntity persistenceEntity;
    private Git git;

    public GitRepositoryAnalyser(PersistenceEntity persistenceEntity, Repository repository) {
        this.persistenceEntity = persistenceEntity;
        this.git = new Git(repository);
    }

    public void analyseRepository() throws GitAPIException, IOException {
        List<String> projectFiles = new ArrayList<>();
        Files.walk(Paths.get(persistenceEntity.getTargetProjectPath())).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                projectFiles.add(filePath.toString().substring(persistenceEntity.getTargetRepositoryPath().length()+1).replace("\\", "/"));
            }
        });

        for (String filePath :
                projectFiles) {
            getLinesInfo(filePath);
        }
    }

    private void getLinesInfo(String filePath) throws GitAPIException, IOException {
        List<Line> lines = persistenceEntity.getLines();
        Map<String, at.ac.tuwien.ifs.qse.model.File> files = persistenceEntity.getFiles();

        BlameCommand blameCommand = git.blame();
        blameCommand = blameCommand.setStartCommit(git.getRepository().resolve("HEAD"));
        blameCommand = blameCommand.setFilePath(filePath);
        BlameResult blameResult  = blameCommand.call();

        Line newLine;

        try {
            RawText rawText = blameResult.getResultContents();
            at.ac.tuwien.ifs.qse.model.File file = new at.ac.tuwien.ifs.qse.model.File(filePath.replace("/", "."));

            for (int i = 0; i < rawText.size(); i++) {
                newLine = new Line(blameResult.getSourceLine(i), file.getFileName());
                newLine.setRevisionNumber(blameResult.getSourceCommit(i).getName());
                newLine.setIssueId(getIssueId(blameResult.getSourceCommit(i).getName()));
                lines.add(newLine);
                file.addLine(newLine);
            }
            files.put(file.getFileName(), file);
        } catch (NullPointerException e){
            //nothing to be done, the file is simply doesn't exist in the repository (ex: .git, .idea,...)
        }
    }

    private String getIssueId(String revisionId) throws GitAPIException, IOException {
        Map<String, Issue> issues = persistenceEntity.getIssues();
        String issueId = null;
        Issue issue;

        String commitsRegEx = persistenceEntity.getCommitsRegEx();
        Pattern pattern = Pattern.compile(".*(" + commitsRegEx + ").*", Pattern.DOTALL);
        Matcher matcher;

        Iterable<RevCommit> log = git.log().
                add(git.getRepository().resolve(revisionId)).call();

        if (log.iterator().hasNext()) {
            RevCommit revCommit = log.iterator().next();
            matcher = pattern.matcher(revCommit.getFullMessage());

            if (matcher.matches()) {
                issueId = matcher.group(1);
                issue = issues.get(issueId);
                if (issue == null){
                    issue = new Issue(issueId);
                }
                issue.addRevisionId(revisionId);
                issues.put(issueId, issue);
            }
        }
        return issueId;
    }

}
