package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

/**
 * Responsible of analysing a repository and extracting information from it.
 * identifies the lines implementing each issue and parses the Issue-Ids
 * from the commit messages.
 */
public interface RepositoryAnalyser {

    /**
     * identifies the lines implementing each issue and parses the Issue-Ids
     * from the commit messages.
     */
    void analyseRepository() throws GitAPIException, IOException;

}
