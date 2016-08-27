package at.ac.tuwien.ifs.qse.service;

import at.ac.tuwien.ifs.qse.model.Class;
import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAccessService {

    private Map<String, Class> classes;
    private Map<String, Issue> issues;
    private List<Line> lines;

    public ModelAccessService() {
        this.classes = new HashMap<String, Class>();
        this.issues = new HashMap<String, Issue>();
        this.lines = new ArrayList<Line>();
    }

    public Map<String, Class> getClasses(){
        return this.classes;
    }

    public Map<String, Issue> getIssues(){
        return this.issues;
    }

    public List<Line> getLines(){
        return this.lines;
    }
}
