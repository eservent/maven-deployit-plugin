package com.xebialabs.deployit.maven.packager;

import com.xebialabs.deployit.maven.ConfigurationItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.Artifact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 29 avr. 2010
 * Time: 18:18:57
 * To change this template use File | Settings | File Templates.
 */
public class ManifestPackager implements ApplicationDeploymentPackager {

    private final File targetDirectory;
    private final String deploymentPackageName;

    private final Manifest manifest = new Manifest();
    private static final String DEPLOYMENT_PACKAGE_DIR = "deployment-package";


    public ManifestPackager(String artifactId, String version, File targetDirectory) {
        this.targetDirectory = new File(targetDirectory, DEPLOYMENT_PACKAGE_DIR + File.separator + artifactId + File.separator + version);
        this.targetDirectory.mkdirs();

        this.deploymentPackageName = artifactId + "/" + version;
        final Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.putValue("Manifest-Version", "1.0");
        mainAttributes.putValue("Deployit-Package-Format-Version", "1.1");
        mainAttributes.putValue("CI-Application", artifactId);
        mainAttributes.putValue("CI-Version", version);
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

        final Map<String, Attributes> entries = manifest.getEntries();
        Attributes attributes = new Attributes();
        attributes.putValue("CI-Type", ciType);
        attributes.putValue("CI-Name", artifact.getArtifactId() + "-" + artifact.getVersion());
        entries.put(type + "/" + FilenameUtils.getName(artifact.getFile().getName()), attributes);


        try {
            FileUtils.copyFileToDirectory(artifact.getFile(), new File(targetDirectory, type));
        } catch (IOException e) {
            throw new RuntimeException("Fail to copy of " + artifact.getFile() + " to " + new File(targetDirectory, type), e);
        }
    }

    public void perform() {
        final File meta_inf = new File(targetDirectory, "META-INF");
        meta_inf.mkdirs();

        File manifestFile = new File(meta_inf, "MANIFEST.MF");
        try {
            FileOutputStream fos = new FileOutputStream(manifestFile);
            manifest.write(fos);
            fos.close();
        } catch (IOException e) {
            new RuntimeException("perform failed", e);
        }
    }

    public String getDeploymentPackageName() {
        return deploymentPackageName;
    }

    public List<String> getCliCommands() {
        List<String> a = new ArrayList<String>();
        a.add("import location=" + targetDirectory);
        a.add("show");
        a.add("show_type");
        a.add("show_type DeploymentPackage");
        return a;
        //return Collections.singletonList("import location="+targetDirectory);
    }

    public void addCI(ConfigurationItem configurationItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
