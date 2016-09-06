package at.ac.tuwien.ifs.qse.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an issue.
 */
public class Issue {

    private String issueId;
    private List<String> revisionIds;
    private int lines;
    private int CoveredLines;
    private int positiveCoveredLines;

    public Issue(String issueId) {
        this.issueId = issueId;
        this.revisionIds = new ArrayList<>();
        lines = 0;
        CoveredLines = 0;
        positiveCoveredLines = 0;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public List<String> getRevisionIds() {
        return revisionIds;
    }

    public void addRevisionId(String revisionId) {
        this.revisionIds.add(revisionId);
    }

    public int getLines() {
        return lines;
    }

    public void incrementLines() {
        this.lines++;
    }

    public int getCoveredLines() {
        return CoveredLines;
    }

    public void incrementCoveredLines() {
        CoveredLines++;
    }

    public int getPositiveCoveredLines() {
        return positiveCoveredLines;
    }

    public void incrementPositiveCoveredLines() {
        this.positiveCoveredLines++;
    }
}
