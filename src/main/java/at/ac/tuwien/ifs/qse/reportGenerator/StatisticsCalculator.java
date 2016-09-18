package at.ac.tuwien.ifs.qse.reportGenerator;

import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.Requirement;
import at.ac.tuwien.ifs.qse.model.TestCase;
import at.ac.tuwien.ifs.qse.persistence.Persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Calculates different statistics about the project.
 */
class StatisticsCalculator {

    private Persistence persistence;

    StatisticsCalculator(Persistence persistence) {
        this.persistence = persistence;
    }

    long getNumberOfPositiveTests() {
        return persistence.getTestCases().stream()
                .filter(TestCase::isPositive)
                .count();
    }

    long countRelevantLines() {
        return persistence.getRelevantLines().size();
    }

    long countCoveredLines() {
        return persistence.getRelevantLines().stream()
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countPositivelyCoveredLines() {
        return persistence.getRelevantLines().stream()
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    long countRelevantLines(String issueId) {
        return persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .count();
    }

    long countCoveredLines(String issueId) {
        return persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countPositivelyCoveredLines(String issueId) {
        return persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    Set<TestCase> getTestCasesForIssue(String issueId) {
        List<Line> relevant = persistence.getRelevantLines().stream()
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

    long countLines(String issueId) {
        return persistence.getAllLines().stream()
                .filter(line -> line.getIssueId() != null)
                .filter(line -> line.getIssueId().equals(issueId))
                .count();
    }

    long countRelevantLines(Requirement requirement) {
        return persistence.getRelevantLines().stream()
                .filter(line ->
                        line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement().equals(requirement))
                .count();
    }

    long countCoveredLines(Requirement requirement) {
        return persistence.getRelevantLines().stream()
                .filter(line ->
                        line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement().equals(requirement))
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countPositivelyCoveredLines(Requirement requirement) {
        return persistence.getRelevantLines().stream()
                .filter(line ->
                        line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement().equals(requirement))
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    long countLines(Requirement requirement) {
        return persistence.getAllLines().stream()
                .filter(line ->
                        line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement().equals(requirement))
                .count();
    }

    long countNotRelatedToRequirementLines() {
        return persistence.getAllLines().stream()
                .filter(line ->
                        (line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() == null) ||
                        (line.getIssueId() == null))
                .count();
    }

    long countNotRelatedToRequirementCoveredLines() {
        return persistence.getRelevantLines().stream()
                .filter(line ->
                        (line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() == null) ||
                        (line.getIssueId() == null))
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countNotRelatedToRequirementRelevantLines() {
        return persistence.getRelevantLines().stream()
                .filter(line ->
                        (line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() == null) ||
                        (line.getIssueId() == null))
                .count();
    }

    long countNotRelatedToRequirementPositivelyCoveredLines() {
        return persistence.getRelevantLines().stream()
                .filter(line ->
                        (line.getIssueId() != null &&
                        persistence.getIssue(line.getIssueId()) != null &&
                        persistence.getIssue(line.getIssueId()).getRequirement() == null) ||
                        (line.getIssueId() == null))
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    long countNotRelatedToIssueLines() {
        return persistence.getAllLines().stream()
                .filter(line -> line.getIssueId() == null)
                .count();
    }

    long countNotRelatedToIssueCoveredLines() {
        return persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() == null)
                .filter(line -> !line.getTestCases().isEmpty())
                .count();
    }

    long countNotRelatedToIssueRelevantLines() {
        return persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() == null)
                .count();
    }

    long countNotRelatedToIssuePositivelyCoveredLines() {
        return persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() == null)
                .filter(line -> !line.getTestCases().isEmpty())
                .filter(line -> line.getTestCases().stream()
                        .allMatch(TestCase::isPositive))
                .count();
    }

    Set<TestCase> getNotRelatedToIssuesTestCases() {
        List<Line> relevant = persistence.getRelevantLines().stream()
                .filter(line -> line.getIssueId() == null)
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
