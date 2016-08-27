package at.ac.tuwien.ifs.qse.model;

import java.util.List;

/**
 * Represents a file.
 */
public class File {

    private String className;
    private List<Line> lines;

    public File(String className) {
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
