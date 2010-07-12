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

package com.xebialabs.deployit.maven.packager;

import com.xebialabs.deployit.maven.DeployableArtifactItem;
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

    void addDeployableArtifact(DeployableArtifactItem item);
}
