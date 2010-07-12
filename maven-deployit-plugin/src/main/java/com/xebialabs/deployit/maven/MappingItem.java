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

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 28 juin 2010
 * Time: 14:20:19
 * To change this template use File | Settings | File Templates.
 */
public class MappingItem extends ConfigurationItem {

    private String source;
    private String target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLabel() {
      return '"'+source + " to "+ target +" for " + DeployMojo.DEFAULT_DEPLOYMENT+'"';
    }

    @Override
    public void addParameter(String name, Object value) {
        if ("source".equals(name) )
            return;

        if ("target".equals(name) )
            return;

        if ("label".equals(name) )
            return;

        super.addParameter(name, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String getCli() {
        return "modify "+getLabel()+ " "+parameters;
    }
}
