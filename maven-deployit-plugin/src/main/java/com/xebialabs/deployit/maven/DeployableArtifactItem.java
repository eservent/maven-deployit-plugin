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

import org.apache.commons.lang.StringUtils;

public class DeployableArtifactItem {

	private String type;

	private String location;

	private String name;

	private String darLocation;

	private boolean folder = false;

	public DeployableArtifactItem() {
	}

	public String getDarLocation() {
		return darLocation;
	}

	public void setDarLocation(String darLocation) {
		this.darLocation = darLocation;
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

	public boolean isFolder() {		
		return folder || "Libraries".equals(type);
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	@Override
	public String toString() {
		return "DeployableArtifactItem{" +
				"type='" + type + '\'' +
				", location='" + location + '\'' +
				", name='" + name + '\'' +
				", darLocation='" + darLocation + '\'' +
				", folder=" + isFolder() +
				'}';
	}

	public boolean hasName() {
		return !StringUtils.isBlank(name);
	}
}
