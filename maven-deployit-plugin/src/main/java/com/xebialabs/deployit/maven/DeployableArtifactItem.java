package com.xebialabs.deployit.maven;

import org.apache.commons.lang.StringUtils;


public class DeployableArtifactItem {

    private String label;

    private String type;
    
    private String location;

    private String name;

    public DeployableArtifactItem() {
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DeployableArtifactItem{" +
                "label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean hasName() {
        return !StringUtils.isBlank(name);
    }
}
