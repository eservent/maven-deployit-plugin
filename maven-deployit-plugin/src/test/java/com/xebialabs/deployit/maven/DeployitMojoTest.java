package com.xebialabs.deployit.maven;


import com.xebia.ad.Server;
import com.xebialabs.deployit.plugin.tomcat.ci.TomcatServer;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 9 juin 2010
 * Time: 09:29:34
 * To change this template use File | Settings | File Templates.
 */
public class DeployitMojoTest extends AbstractMojoTestCase {

    private DeployMojo mojo;


    public void setUp() throws Exception {
        super.setUp();
        File dot = new File(".");
        System.out.println(dot.getAbsolutePath());
        new File(dot, "deployit.hdb.log").delete();
        new File(dot, "deployit.hdb.script").delete();
        new File(dot, "deployit.hdb.properties").delete();
        new File(dot, "deployit.conf");
        mojo = new DeployMojo();
    }


    /*
        public void testEmptyEnvMojo()
                throws Exception {
            MavenProjectStub project = new MavenProjectStub();
            ArtifactStub mainArtifact = new ArtifactStub();
            mainArtifact.setType("War");
            mainArtifact.setArtifactId("com.test");
            mainArtifact.setGroupId("test");
            mainArtifact.setVersion("1.0");
            mainArtifact.setFile(new File("main.war"));
            project.setArtifact(mainArtifact);
            setVariableValueToObject(mojo,"environment",Collections.<ConfigurationItem>emptyList());
            setVariableValueToObject(mojo,"project",project);
            setVariableValueToObject(mojo,"artifactId","com.test");
            setVariableValueToObject(mojo,"version","1.0");
            mojo.execute();
        }
    */

    @Test
    public void testOneServerEnvMojo()
            throws Exception {
        MavenProjectStub project = new MavenProjectStub();
        ArtifactStub mainArtifact = new ArtifactStub();
        mainArtifact.setType("War");
        mainArtifact.setArtifactId("com.test.tomcat");
        mainArtifact.setGroupId("test");
        mainArtifact.setVersion("1.0");
        mainArtifact.setFile(new File("main.war"));
        project.setArtifact(mainArtifact);

        ConfigurationItem host = new ConfigurationItem();
        host.setMainType("Host");
        host.addParameter("label", "Tomcat Host");
        host.addParameter("address", "ubuntu-weblogic-1.local");
        host.addParameter("username", "weblogic");
        host.addParameter("password", "weblogic");
        host.addParameter("operatingSystemFamily", "UNIX");
        host.addParameter("accessMethod", "SSH_SCP");


        TomcatServer t = new TomcatServer();

        ConfigurationItem server = new ConfigurationItem();
        server.setMainType("TomcatServer");
        server.addParameter("host", "Tomcat Host");
        server.addParameter("label", "tomcat server");
        server.addParameter("port", "8080");
        server.addParameter("accessMethod", "http");
        server.addParameter("tomcatHome", "/opt/apache-tomcat-6.0.26");
        server.addParameter("startCommand", "/opt/apache-tomcat-6.0.26/bin/catalina.sh start");
        server.addParameter("stopCommand", "/opt/apache-tomcat-6.0.26/bin/catalina.sh stop");
        server.addParameter("appBase", "/opt/apache-tomcat-6.0.26/mywebapps");
        server.addParameter("baseUrl", "http://ubuntu-weblogic-1.local:8080");
        server.addParameter("managerAppInstalled", "false");
        server.addParameter("ajpport", "8009");

        List<ConfigurationItem> env = new ArrayList<ConfigurationItem>();
        env.add(host);
        env.add(server);

        setVariableValueToObject(mojo, "testmode", true);
        setVariableValueToObject(mojo, "environment", env);
        setVariableValueToObject(mojo, "project", project);
        setVariableValueToObject(mojo, "artifactId", "com.test.tomcat");
        setVariableValueToObject(mojo, "version", "1.0");
        try {
            mojo.execute();
        } catch (Exception e) {
            System.out.println(mojo.getScript());
            throw e;
        }
    }


    @After
    public void tearDown() {
        Server.shutdown();
    }


}
