package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import at.ac.tuwien.ifs.qse.model.Issue;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

public interface RepositoryAnalyser {

    /**
     * Iterates over all files in the given Repository and Returns the
     * Issues. The Issues should contain the lines implementing them.
     * @param repository the repository to analyze
     * @return a list of issues containing the lines implementing them
     */
    List<Issue> analyseRepository(Repository repository);

}
