package at.ac.tuwien.ifs.qse.model;

import java.util.List;

/**
 * Represents a file.
 */
public class File {

    private String fileName;
    private List<Line> lines;

    public File(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }
}
