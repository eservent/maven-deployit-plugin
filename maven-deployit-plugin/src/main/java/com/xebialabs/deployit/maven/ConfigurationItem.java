/*
 * This file is part of Maven Deployit plugin.
 *
 * Maven Deployit plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Maven Deployit plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Maven Deployit plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

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
