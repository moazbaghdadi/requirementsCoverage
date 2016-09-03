package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.File;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;
import at.ac.tuwien.ifs.qse.model.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides singleton lists to store information in.
 */
public class PersistenceEntity {

    private Map<String, File> files;
    private Map<String, Issue> issues;
    private Map<String, TestCase> testCases;
    private List<Line> lines;
    private String targetProjectPath;
    private String commitsRegEx;

    public PersistenceEntity(String targetProjectPath, String commitsRegEx) {
        this.targetProjectPath = targetProjectPath;
        this.commitsRegEx = commitsRegEx;
    }

    public Map<String, File> getFiles(){
        if (files == null) {
            files = new HashMap<>();
        }
        return files;
    }

    public Map<String, Issue> getIssues(){
        if (issues == null) {
            issues = new HashMap<>();
        }
        return issues;
    }

    public Map<String, TestCase> getTestCases() {
        if (testCases == null) {
            testCases = new HashMap<>();
        }
        return testCases;
    }

    public List<Line> getLines(){
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    public String getTargetProjectPath(){
        return targetProjectPath;
    }

    public String getCommitsRegEx() {
        return commitsRegEx;
    }
}
