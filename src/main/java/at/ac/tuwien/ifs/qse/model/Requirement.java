package at.ac.tuwien.ifs.qse.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a requirement.
 */
public class Requirement {

    private String requirementId;
    private List<Issue> issues;

    public Requirement(String requirementId) {
        this.requirementId = requirementId;
        this.issues = new ArrayList<>();
    }

    public String getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(String requirementId) {
        this.requirementId = requirementId;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void addIssue(Issue issue) {
        this.issues.add(issue);
    }
}
