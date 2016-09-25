package at.ac.tuwien.ifs.qse.xmlParser;

import at.ac.tuwien.ifs.qse.model.Issue;
import at.ac.tuwien.ifs.qse.model.Requirement;
import at.ac.tuwien.ifs.qse.persistence.Persistence;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX handler for requirements
 */
public class RequirementsSAXHandler extends DefaultHandler {

    private Requirement requirement;
    private Persistence persistence;

    public RequirementsSAXHandler (Persistence persistence) {
        this.persistence = persistence;
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("requirement")){
            requirement = new Requirement(attributes.getValue("name"));
        } else if (qualifiedName.equals("issue")){
            Issue issue = persistence.getIssue(attributes.getValue("id"));
            if (issue == null) {
                issue = new Issue(attributes.getValue("id"));
            }
            issue.setRequirement(requirement);
            persistence.addIssue(issue);
            requirement.addIssue(issue);
        }
    }

    public void endElement (String namespaceURI,
                            String localName,
                            String qualifiedName) throws SAXException {
         if (qualifiedName.equals("requirement")) {
             persistence.addRequirement(requirement);
         }
    }
}
