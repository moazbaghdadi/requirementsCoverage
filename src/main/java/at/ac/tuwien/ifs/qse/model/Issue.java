package at.ac.tuwien.ifs.qse.model;

import java.util.ArrayList;
import java.util.List;

public class Issue {

    private String issueId;
    private List<Line> lines;

    public Issue(String issueId) {
        this.issueId = issueId;
        this.lines = new ArrayList<Line>();
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }
}
