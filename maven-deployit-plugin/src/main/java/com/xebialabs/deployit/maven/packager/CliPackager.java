package com.xebialabs.deployit.maven.packager;

import com.xebialabs.deployit.maven.DeployableArtifactItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Packager using CLI interface
 */
public class CliPackager implements ApplicationDeploymentPackager {
    private final String artifactId;
    private final String version;


    private final List<String> cliCommands = new ArrayList<String>();

    public CliPackager(String artifactId, String version) {
        this.artifactId = artifactId;
        this.version = version;

        addCommand("create Application label=\"" + artifactId + "\"");
        String createDeploymentPackage = "create DeploymentPackage label=\"" + artifactId + "\" application=\"" + artifactId
                + "\" version=\"" + version + "\"";
        addCommand(createDeploymentPackage);
    }

    public void addMavenArtifact(Artifact artifact) {
        String ciType = null;
        String type = artifact.getType();

        if (type.compareToIgnoreCase("ear") == 0) {
            ciType = "Ear";
        } else if (type.compareToIgnoreCase("war") == 0) {
            ciType = "War";
        } else {
            System.out.println("Not supported type [" + ciType + "], skit it");
            return;
        }

        final File archiveFile = artifact.getFile();
        if (archiveFile == null)
            throw new RuntimeException("No file found for " + artifact);

        String ciLabel = getDeploymentPackageName() + "/" + archiveFile.getName();

        addCommand("create " + ciType + " label=\"" + ciLabel + "\" name=\""
                + FilenameUtils.getBaseName(archiveFile.getName()) + "\" location=\"" + archiveFile.getAbsolutePath() + "\"");
        addCommand("modify \"" + getDeploymentPackageName() + "\" deployableArtifacts+=\"" + ciLabel + "\"");
    }

    public void perform() {

    }

    public String getDeploymentPackageName() {
        return this.artifactId + " - " + this.version;
    }

    public List<String> getCliCommands() {
        return cliCommands;
    }

    public void addDeployableArtifact(DeployableArtifactItem item) {
        String createCI = "create \"" + item.getType() + "\" label=\"" + item.getLabel() + "\" location=\"" + item.getLocation() + "\"";
        if (item.hasName()) {
            createCI += " name=\"" + item.getName() + "\"";
        }
        addCommand(createCI);
        addCommand("modify \"" + getDeploymentPackageName() + "\" deployableArtifacts+=\"" + item.getLabel() + "\"");
    }

    private void addCommand(String cmdLine) {
        cliCommands.add(cmdLine);
    }

}
