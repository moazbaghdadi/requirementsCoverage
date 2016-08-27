package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import at.ac.tuwien.ifs.qse.model.Issue;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

/**
 * Responsible of analysing a repository and extracting information from it.
 * identifies the lines implementing each issue and parses the Issue-Ids
 * from the commit messages.
 */
public interface RepositoryAnalyser {

    /**
     * identifies the lines implementing each issue and parses the Issue-Ids
     * from the commit messages.
     * @param repository the repository to analyze
     */
    void analyseRepository(Repository repository);

}
