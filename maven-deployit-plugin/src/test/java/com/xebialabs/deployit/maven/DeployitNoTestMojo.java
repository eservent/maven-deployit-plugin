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


import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DeployitNoTestMojo extends AbstractMojoTestCase {

	private DeployMojo mojo;
	private ConfigurationItem host;
	private ConfigurationItem tomcatServer;
	private DeployableArtifactItem configurationFiles;
	private static final boolean IGNORE = true;


	public void setUp() throws Exception {
		super.setUp();
		File dot = new File(".");
		System.out.println(dot.getAbsolutePath());
		new File(dot, "deployit.hdb.log").delete();
		new File(dot, "deployit.hdb.script").delete();
		new File(dot, "deployit.hdb.properties").delete();
		new File(dot, "deployit.conf");


		host = new ConfigurationItem();
		host.setMainType("Host");
		host.addParameter("label", "Tomcat Host");
		host.addParameter("address", "ubuntu-weblogic-1.local");
		host.addParameter("username", "weblogic");
		host.addParameter("password", "weblogic");
		host.addParameter("operatingSystemFamily", "UNIX");
		host.addParameter("accessMethod", "SSH_SCP");

		tomcatServer = new ConfigurationItem();
		tomcatServer.setMainType("TomcatUnmanagedServer");
		tomcatServer.addParameter("host", "Tomcat Host");
		tomcatServer.addParameter("label", "tomcat server");
		tomcatServer.addParameter("port", "8080");
		tomcatServer.addParameter("accessMethod", "http");
		tomcatServer.addParameter("tomcatHome", "/opt/apache-tomcat-6.0.26");
		tomcatServer.addParameter("startCommand", "/opt/apache-tomcat-6.0.26/bin/catalina.sh start");
		tomcatServer.addParameter("stopCommand", "/opt/apache-tomcat-6.0.26/bin/catalina.sh stop");
		tomcatServer.addParameter("appBase", "/opt/apache-tomcat-6.0.26/mywebapps");
		tomcatServer.addParameter("baseUrl", "http://ubuntu-weblogic-1.local:8080");
		tomcatServer.addParameter("managerAppInstalled", "false");
		tomcatServer.addParameter("ajpport", "8009");


		configurationFiles = new DeployableArtifactItem();
		configurationFiles.setType("ConfigurationFiles");

		configurationFiles.setName("ConfigurationFilesCIName");
		configurationFiles.setLocation("src/main/resources");


		mojo = new DeployMojo();
	}


	@Test
	@Ignore
	public void testOneServerEnvMojo()
			throws Exception {

		if (IGNORE)
			return ;

		MavenProjectStub project = new MavenProjectStub();
		ArtifactStub mainArtifact = new ArtifactStub();
		mainArtifact.setType("War");
		mainArtifact.setArtifactId("com.test.tomcat");
		mainArtifact.setGroupId("test");
		mainArtifact.setVersion("1.0");
		mainArtifact.setFile(new File("main.war"));
		project.setArtifact(mainArtifact);


		List<ConfigurationItem> env = new ArrayList<ConfigurationItem>();
		env.add(host);
		env.add(tomcatServer);


		setVariableValueToObject(mojo, "testmode", true);
		setVariableValueToObject(mojo, "environment", env);
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "artifactId", "com.test.tomcat");
		setVariableValueToObject(mojo, "version", "1.0");
		setVariableValueToObject(mojo, "generateManifestOnly", true);

		try {
			mojo.execute();
		} finally {
			System.out.println(mojo.getScript());
		}


	}


	@Test
	@Ignore
	public void testOneServerEnvMojoWithConfigurationFiles()
			throws Exception {

		if (IGNORE)
			return ;
		
		MavenProjectStub project = new MavenProjectStub();
		ArtifactStub mainArtifact = new ArtifactStub();
		mainArtifact.setType("War");
		mainArtifact.setArtifactId("com.test.tomcat");
		mainArtifact.setGroupId("test");
		mainArtifact.setVersion("1.0");
		mainArtifact.setFile(new File("main.war"));
		project.setArtifact(mainArtifact);


		List<ConfigurationItem> env = new ArrayList<ConfigurationItem>();
		env.add(host);
		env.add(tomcatServer);


		MappingItem mapping = new MappingItem();
		mapping.setMainType("ConfigurationFilesMapping");
		mapping.setTarget(host.getLabel());
		mapping.setSource(configurationFiles.getName());
		mapping.addParameter("targetdirectory", "/tmp/remoteproperties");


		setVariableValueToObject(mojo, "testmode", true);
		setVariableValueToObject(mojo, "environment", env);
		setVariableValueToObject(mojo, "deployableArtifacts", Collections.singletonList(configurationFiles));
		setVariableValueToObject(mojo, "mappings", Collections.singletonList(mapping));
		setVariableValueToObject(mojo, "project", project);
		setVariableValueToObject(mojo, "artifactId", "com.test.tomcat");
		setVariableValueToObject(mojo, "version", "1.0");
		setVariableValueToObject(mojo, "generateManifestOnly", true);

		try {
			mojo.execute();
		} finally {
			System.out.println(mojo.getScript());
		}
	}


	@After
	public void tearDown() {

		if (IGNORE)
			return ;
		
		DeployMojo.stopServer();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

	}


}
