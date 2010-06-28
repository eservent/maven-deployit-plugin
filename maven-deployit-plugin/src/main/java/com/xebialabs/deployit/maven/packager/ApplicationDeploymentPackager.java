package com.xebialabs.deployit.maven.packager;

import com.xebialabs.deployit.maven.ConfigurationItem;
import org.apache.maven.artifact.Artifact;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 29 avr. 2010
 * Time: 18:35:33
 * To change this template use File | Settings | File Templates.
 */
public interface ApplicationDeploymentPackager {
    void addMavenArtifact(Artifact artifact);

    void perform();

    String getDeploymentPackageName();

    List<String> getCliCommands();

    void addCI(ConfigurationItem configurationItem);
}
