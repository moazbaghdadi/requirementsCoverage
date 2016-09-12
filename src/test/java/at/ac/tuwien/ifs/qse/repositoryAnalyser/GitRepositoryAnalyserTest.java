package at.ac.tuwien.ifs.qse.repositoryAnalyser;

import at.ac.tuwien.ifs.qse.persistence.PersistenceEntity;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Tests the class GitRepositoryAnalyser.
 */
public class GitRepositoryAnalyserTest {
    private PersistenceEntity persistenceEntity;

    @Before
    public void setUp() throws Exception {
        persistenceEntity = new PersistenceEntity(
                "D:/project/qpid-java",
                "D:/project/qpid-java/bdbstore",
                "QPID-\\d+");
        Repository repository = new FileRepository(persistenceEntity.getTargetRepositoryPath() + "/.git");

        RepositoryAnalyser gitRepositoryAnalyser = new GitRepositoryAnalyser(persistenceEntity, repository);
        gitRepositoryAnalyser.analyseRepository();
    }

    @Test
    public void testAnalyseRepository_CheckNumberOfFiles() throws Exception {
        assertEquals(150, persistenceEntity.getFiles().size());
    }

    @Test
    public void testAnalyseRepository_CheckNumberOfLines() throws Exception {
        int lines = 0;
        for (Path filePath :
                Files.walk(Paths.get(persistenceEntity.getTargetProjectPath())).collect(Collectors.toList())) {
            if (Files.isRegularFile(filePath)) {
                try {
                    lines += count(filePath.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        assertEquals(lines, persistenceEntity.getAllLines().size());
    }

    @Test
    public void testAnalyseRepository_CheckNumberOfIssues() throws Exception {
        assertEquals(284, persistenceEntity.getIssues().size());
    }

    private int count(File filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if (endsWithoutNewLine) {
                ++count;
            }
            return count;
        }
    }
}