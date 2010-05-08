package com.xebialabs.deployit.maven;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 21 avr. 2010
 * Time: 15:38:09
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationItem {

    private String mainType;
    private final StringBuilder parameters = new StringBuilder();
    private String label;

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

    @Override
    public String toString() {
        return "ConfigurationItem{" +
                "mainType='" + mainType + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
