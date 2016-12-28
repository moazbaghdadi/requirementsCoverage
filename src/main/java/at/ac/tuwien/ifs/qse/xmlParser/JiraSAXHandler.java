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
public class JiraSAXHandler extends DefaultHandler {

    private Requirement requirement;
    private Persistence persistence;
    private boolean storeIssueId = false;
    private String issueId;

    public JiraSAXHandler(Persistence persistence) {
        this.persistence = persistence;
    }

    public void startElement (String namespaceURI,
                              String localName,
                              String qualifiedName,
                              Attributes attributes) throws SAXException {
        if (qualifiedName.equals("project")){
            requirement = persistence.getRequirement(attributes.getValue("key"));
            if (requirement == null) {
                requirement = new Requirement(attributes.getValue("key"));
            }
        } else if (qualifiedName.equals("key")){
            storeIssueId = true;
        }
    }

    public void characters(char[] text, int start, int length)
            throws SAXException {
        if (storeIssueId){
            issueId = new String(text, start, length);
        }
    }

    public void endElement (String namespaceURI,
                            String localName,
                            String qualifiedName) throws SAXException {
         if (qualifiedName.equals("item")) {
             persistence.addRequirement(requirement);
         } else if (qualifiedName.equals("key")) {
             storeIssueId = false;
             //System.out.println("storing requirement: " + requirement.getRequirementId() + ", and issue: " + issueId);

             Issue issue = persistence.getIssue(issueId);
             if (issue == null) {
                 issue = new Issue(issueId);
             }
             issue.setRequirement(requirement);
             persistence.addIssue(issue);
             requirement.addIssue(issue);
         }
    }
}
