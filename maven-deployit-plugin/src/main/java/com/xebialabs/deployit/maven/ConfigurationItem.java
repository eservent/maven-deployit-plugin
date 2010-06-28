package com.xebialabs.deployit.maven;

public class ConfigurationItem {

    private String mainType;
    protected final StringBuilder parameters = new StringBuilder();
    private String label;

    private boolean addedToEnvironment = true;

    public String getMainType() {
        return mainType;
    }

    public String getLabel() {
        if (label == null) {
            label = "autogen-label-"+mainType;
        }
        return label;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public void addParameter(String name, Object value) {
        if ("addedToEnvironment".equals(name)){
            addedToEnvironment = Boolean.parseBoolean(value.toString());
            return;
        }
        parameters.append(' ').append(name).append('=').append('"').append(value).append('"').append(' ');
        if ("label".equals(name)) {
            label = value.toString();
        }

    }

    public String getCli() {
        if (label == null) {
             return "create " + mainType + " " + parameters; // RAJOUTER !!!!            
        }
        return "create " + mainType + " " + parameters;
    }

    public boolean isAddedToEnvironment() {
        return addedToEnvironment;
    }

    public void setAddedToEnvironment(boolean addedToEnvironment) {
        this.addedToEnvironment = addedToEnvironment;
    }

    @Override
    public String toString() {
        return "ConfigurationItem{" +
                "mainType='" + mainType + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
