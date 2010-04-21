package com.xebialabs.experiments.maven.plugin.deployit;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 20 avr. 2010
 * Time: 18:02:31
 * To change this template use File | Settings | File Templates.
 */
public class Module {
    private String artifactId;
    private String groupId;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return groupId+" - "+artifactId;
    }
}
