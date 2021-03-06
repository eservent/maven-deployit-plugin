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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestPackager implements ApplicationDeploymentPackager {

	private final File targetDirectory;
	private final String deploymentPackageName;

	private final Manifest manifest = new Manifest();
	private static final String DEPLOYMENT_PACKAGE_DIR = "deployment-package";

	private boolean generateManifestOnly = false;


	public File getTargetDirectory() {
		return targetDirectory;
	}

	public ManifestPackager(String artifactId, String version, File targetDirectory) {
		this.targetDirectory = new File(targetDirectory, DEPLOYMENT_PACKAGE_DIR + File.separator + artifactId + File.separator + version);
		this.targetDirectory.mkdirs();

		this.deploymentPackageName = artifactId + "/" + version;
		final Attributes mainAttributes = manifest.getMainAttributes();
		mainAttributes.putValue("Manifest-Version", "1.0");
		mainAttributes.putValue("Deployit-Package-Format-Version", "1.1");
		mainAttributes.putValue("CI-Application", artifactId);
		mainAttributes.putValue("CI-Version", version);
	}

	public void perform() {
		final File meta_inf = new File(targetDirectory, "META-INF");
		meta_inf.mkdirs();

		File manifestFile = new File(meta_inf, "MANIFEST.MF");
		try {
			FileOutputStream fos = new FileOutputStream(manifestFile);
			manifest.write(fos);
			fos.close();
		} catch (IOException e) {
			new RuntimeException("perform failed", e);
		}
	}

	public String getDeploymentPackageName() {
		return deploymentPackageName;
	}

	public List<String> getCliCommands() {
		List<String> a = new ArrayList<String>();
		a.add("import location=" + targetDirectory);
		a.add("show");
		a.add("show_type");
		a.add("show_type DeploymentPackage");
		return a;
	}

	public void addDeployableArtifact(DeployableArtifactItem item) {		
		if ("Dar".equals(item.getType()))
			return;

		if ("Pom".equals(item.getType()))
			return;

		final Map<String, Attributes> entries = manifest.getEntries();
		final Attributes attributes = new Attributes();
		final String type = item.getType();
		final File location = new File(item.getLocation());

		attributes.putValue("CI-Type", type);
		if (item.hasName())
			attributes.putValue("CI-Name", item.getName());

		String darLocation = (item.getDarLocation() == null ? type : item.getDarLocation());

		if (item.isFolder()) {
		   entries.put(darLocation, attributes);
		} else {
			if (location.isAbsolute())
				entries.put(darLocation + "/" + location.getName(), attributes);
			else
				entries.put(darLocation + "/" + item.getLocation(), attributes);
		}

		final File targetDir = new File(targetDirectory, darLocation);
		if (generateManifestOnly) {
			System.out.println("Skip copying artifact " + item.getName() + " to " + targetDir);
			return;
		}
		targetDir.mkdirs();

		File locationTargetDirs;
		//do not create missing directories is there are no parents or if the file is absolute
		if (location.isAbsolute() || location.getParent() == null) {
			locationTargetDirs = targetDir;
		} else {
			locationTargetDirs = new File(targetDir, location.getParent());
			locationTargetDirs.mkdirs();
		}


		try {
			if (location.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(location, locationTargetDirs);
			} else {
				FileUtils.copyFileToDirectory(location, locationTargetDirs);
			}
		} catch (IOException e) {
			throw new RuntimeException("Fail to copy of " + location + " to " + targetDir, e);

		}

	}

	public boolean isGenerateManifestOnly() {
		return generateManifestOnly;
	}

	public void setGenerateManifestOnly(boolean generateManifestOnly) {
		this.generateManifestOnly = generateManifestOnly;
	}

	public File getManifestFile() {
		return new File(targetDirectory, "META-INF/MANIFEST.MF");
	}

}
