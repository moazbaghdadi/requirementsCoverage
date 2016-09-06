package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.service.PersistenceEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Calculates different statistics about the project.
 */
class StatisticsCalculator {

    private PersistenceEntity persistenceEntity;

    StatisticsCalculator(PersistenceEntity persistenceEntity) {
        this.persistenceEntity = persistenceEntity;
    }

    long getNumberOfPositiveTests() {
        return persistenceEntity.getTestCases().values().stream()
                .filter(TestCase::isPositive)
                .count();
    }

    long countRelevantLines() {
        return persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .count();
    }

    long countCoveredLines() {
        return persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countPositivelyCoveredLines() {
        return persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    long countRelevantLines(String issueId) {
        return persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .count();
    }

    long countCoveredLines(String issueId) {
        return persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countPositivelyCoveredLines(String issueId) {
        return persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    Set<TestCase> getTestCasesForIssue(String issueId) {
        List<Line> relevant = persistenceEntity.getLines().stream()
                .filter(Line::isRelevant)
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .filter(line -> !line.getTestCases().isEmpty())
                .collect(Collectors.toList());
        Set<TestCase> testCases = new HashSet<>();
        for (Line line :
                relevant) {
            testCases.addAll(line.getTestCases());
        }
        return testCases;
    }
}
