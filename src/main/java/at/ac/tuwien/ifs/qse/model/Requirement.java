package at.ac.tuwien.ifs.qse.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a requirement.
 */
public class Requirement implements Comparable<Requirement>{

    private String requirementId;
    private Set<Issue> issues;

    public Requirement(String requirementId) {
        this.requirementId = requirementId;
        this.issues = new HashSet<>();
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public void addIssue(Issue issue) {
        issues.add(issue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Requirement that = (Requirement) o;

        return requirementId.equals(that.requirementId);

    }

    @Override
    public int hashCode() {
        return requirementId.hashCode();
    }

    @Override
    public int compareTo(Requirement o) {
        return requirementId.compareTo(o.getRequirementId());
    }
}
