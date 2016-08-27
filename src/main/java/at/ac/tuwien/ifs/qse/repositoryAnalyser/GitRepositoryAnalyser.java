package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

public class GitRepositoryAnalyser implements RepositoryAnalyser {

    public List<Issue> analyseRepository(Repository repository) {
        //TODO: implement method
        return null;
    }

    private List<Line> getLineInfoFromFiles(){
        //TODO: implement method
        return null;
    }

    private List<Issue> getIssueIdOfRevisions(){
        //TODO: implement method
        return null;
    }

}
