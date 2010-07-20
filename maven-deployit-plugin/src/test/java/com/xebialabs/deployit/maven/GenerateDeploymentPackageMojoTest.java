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
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateDeploymentPackageMojoTest extends AbstractMojoTestCase {

    private GenerateDeploymentPackageMojo mojo;
    private DeployableArtifactItem configurationFiles;
    private DeployableArtifactItem sqlFiles;


    public void setUp() throws Exception {
        super.setUp();

        configurationFiles = new DeployableArtifactItem();
        configurationFiles.setType("ConfigurationFiles");

        configurationFiles.setLocation("src/main/resources");


        sqlFiles = new DeployableArtifactItem();
        sqlFiles.setType("SqlFiles");
        
        sqlFiles.setLocation("src/main/sql");


        mojo = new GenerateDeploymentPackageMojo();
    }

    @Test
    public void testPackageOne() throws Exception {
        MavenProjectStub project = new MavenProjectStub();
        ArtifactStub mainArtifact = new ArtifactStub();
        mainArtifact.setType("War");
        mainArtifact.setArtifactId("com.test.tomcat");
        mainArtifact.setGroupId("test");
        mainArtifact.setVersion("1.0");
        mainArtifact.setFile(new File("main.war"));
        project.setArtifact(mainArtifact);

        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "outputDirectory", new File("target/"));
        setVariableValueToObject(mojo, "artifactId", "com.test.tomcat");
        setVariableValueToObject(mojo, "version", "1.0");
        setVariableValueToObject(mojo, "jarArchiver", new JarArchiver());
        setVariableValueToObject(mojo, "generateManifestOnly", true);
        setVariableValueToObject(mojo, "finalName","test1");

        try {
            mojo.execute();
        } finally {
            System.out.println(mojo.getScript());
        }


    }

    @Test
    public void testPackageOneWithConfigurationFiles() throws Exception {
        MavenProjectStub project = new MavenProjectStub();
        ArtifactStub mainArtifact = new ArtifactStub();
        mainArtifact.setType("War");
        mainArtifact.setArtifactId("com.test.tomcat.configurationsfiles");
        mainArtifact.setGroupId("test");
        mainArtifact.setVersion("1.0");
        mainArtifact.setFile(new File("main.war"));
        project.setArtifact(mainArtifact);

        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "outputDirectory", new File("target/"));
        setVariableValueToObject(mojo, "artifactId", "com.test.tomcat.configurationsfiles");
        setVariableValueToObject(mojo, "deployableArtifacts", Collections.singletonList(configurationFiles));
                          setVariableValueToObject(mojo, "jarArchiver", new JarArchiver());
        setVariableValueToObject(mojo, "version", "1.0");
        setVariableValueToObject(mojo, "generateManifestOnly", true);


        try {
            mojo.execute();
        } finally {
            System.out.println(mojo.getScript());
        }


    }



    @Test
    public void testPackageOneWithConfigurationFilesAndSqlFiles() throws Exception {
        MavenProjectStub project = new MavenProjectStub();
        ArtifactStub mainArtifact = new ArtifactStub();
        mainArtifact.setType("War");
        mainArtifact.setArtifactId("com.test.tomcat.configurationsfiles");
        mainArtifact.setGroupId("test");
        mainArtifact.setVersion("1.0");
        mainArtifact.setFile(new File("main.war"));
        project.setArtifact(mainArtifact);

        List<DeployableArtifactItem> daitem = new ArrayList<DeployableArtifactItem>();
        daitem.add(configurationFiles);
        daitem.add(sqlFiles);

        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "outputDirectory", new File("target/"));
        setVariableValueToObject(mojo, "artifactId", "com.test.tomcat.configurationsfiles");
        setVariableValueToObject(mojo, "deployableArtifacts", daitem);
                     setVariableValueToObject(mojo, "jarArchiver", new JarArchiver());
        setVariableValueToObject(mojo, "version", "1.0");
        setVariableValueToObject(mojo, "generateManifestOnly", true);


        try {
            mojo.execute();
        } finally {
            System.out.println(mojo.getScript());
        }


    }

}
