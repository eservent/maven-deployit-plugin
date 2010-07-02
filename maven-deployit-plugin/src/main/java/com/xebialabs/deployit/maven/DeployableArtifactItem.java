package com.xebialabs.deployit.maven;


public class DeployableArtifactItem {

    private String label;

    private String type;
    
    private String location;

    public DeployableArtifactItem() {
    }

    public DeployableArtifactItem(String label, String type, String location) {
        this.label = label;
        this.type = type;
        this.location = location;
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

    @Override
    public String toString() {
        return "DeployableArtifactItem{" +
                "label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
