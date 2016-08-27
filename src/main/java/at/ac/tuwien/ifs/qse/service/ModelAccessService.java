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
public class ModelAccessService {

    private static Map<String, File> files;
    private static Map<String, Issue> issues;
    private static Map<String, TestCase> testCases;
    private static List<Line> lines;

    private ModelAccessService() {
    }

    public static Map<String, File> getFiles(){
        if (files == null) {
            files = new HashMap<String, File>();
        }
        return files;
    }

    public static Map<String, Issue> getIssues(){
        if (issues == null) {
            issues = new HashMap<String, Issue>();
        }
        return issues;
    }

    public static Map<String, TestCase> getTestCases() {
        if (testCases == null) {
            testCases = new HashMap<String, TestCase>();
        }
        return testCases;
    }

    public static List<Line> getLines(){
        if (lines == null) {
            lines = new ArrayList<Line>();
        }
        return lines;
    }
}
