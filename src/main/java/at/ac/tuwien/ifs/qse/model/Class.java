package at.ac.tuwien.ifs.qse.model;

import java.util.List;

public class Class {

    private String className;
    private List<Line> lines;

    public Class(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }
}
