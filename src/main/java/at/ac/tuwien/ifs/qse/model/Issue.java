package at.ac.tuwien.ifs.qse.model;

/**
 * Represents an issue.
 */
public class Issue implements Comparable<Issue> {

    private String issueId;
    private Requirement requirement;

    public Issue(String issueId) {
        this.issueId = issueId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        return issueId.equals(issue.issueId);

    }

    @Override
    public int hashCode() {
        return issueId.hashCode();
    }

    @Override
    public int compareTo(Issue issue) {
        return issueId.compareTo(issue.getIssueId());
    }
}
