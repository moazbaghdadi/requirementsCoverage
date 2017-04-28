## Required tools:
the machine running the program should be running on windows and have the following tools installed:
* [Java](https://www.java.com/) >= 8
* [Maven](https://maven.apache.org/)

## Conditions of the target project:
The target project should be:
* a java project.
* using Maven as a build manager tool.
* using Git as a version management system.
* having the [JaCoCo](http://www.eclemma.org/jacoco/) library configured as a dependency in the target project and runnable over maven.
* having a .xml file that includes the relation between the requirements and the issues.

## Building the Project:
To build the project after cloning or downloading it, navigate in the command line tool to the project directory and run the maven command: `mvn install`. Maven will automatically download and install the required libraries and dependencies and compile the project.
After a successful run of the previous command the program can be started with no further settings.

## Starting the Project:
The following command runs the project 
`mvn exec:java -Dexec.mainClass=at.ac.tuwien.ifs.qse.RequirementsCoverageLauncher -Dexec.args="*arg1 arg2 arg3 arg4*"`.

where:
* arg1: the path to the target project directory (where the .git/ folder exists)
* arg2: the path to the target sub-project directory (where the .pom.xml file exists). This directory can be identical with the previous one.
* arg3: the path to the requirements.xml file of the target project
* arg4: the regular expression form of the issue-ids as in the git-commit messages of the target project.

## Results:
After running the project, the resulted requirements coverage report can be found under `./target/requirementsCoverage/index.html`
