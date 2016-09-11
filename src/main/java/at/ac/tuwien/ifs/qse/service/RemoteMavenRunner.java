package at.ac.tuwien.ifs.qse.service;

import org.apache.maven.shared.invoker.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Runns the given maven goals on the given project.
 */
public class RemoteMavenRunner {

    /**
     * Runs the given maven goals on the given project.
     * @param pathToPomFile the path to pom.xml.
     * @param goals the maven goals and arguments to be run.
     * @throws MavenInvocationException If a failure happens while invoking maven
     */
    public static void runRemoteMaven(String pathToPomFile, List<String> goals) throws MavenInvocationException, IOException {
        InvocationRequest request = new DefaultInvocationRequest();
        Invoker invoker = new DefaultInvoker();

        request.setPomFile(new File(pathToPomFile));
        request.setGoals(goals);
        invoker.setMavenHome(detectMavenHome());

        invoker.execute(request);
    }

    /**
     * detects the path to Maven Home.
     * @return a File to Maven Home path
     * @throws IOException If an I/O error occurs
     */
    private static File detectMavenHome() throws IOException {
        //TODO: make it work for all OSs
        Process p = Runtime.getRuntime().exec("cmd /C mvn -v");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Maven home: ")) {
                return new File(line.substring("Maven home: ".length()));
            }
        }
        return null;
    }
}
