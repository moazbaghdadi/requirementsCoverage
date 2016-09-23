package at.ac.tuwien.ifs.qse.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a file.
 */
public class File {

    private String fileName;
    private Set<Line> lines;

    public File(String fileName) {
        this.fileName = fileName;
        this.lines = new HashSet<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        return fileName.equals(file.fileName);

    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }
}
