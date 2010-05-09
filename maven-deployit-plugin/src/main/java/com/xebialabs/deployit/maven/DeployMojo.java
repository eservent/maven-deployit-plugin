package com.xebialabs.deployit.maven;

import com.xebialabs.deployit.maven.packager.ApplicationDeploymentPackager;
import com.xebialabs.deployit.maven.packager.CliPackager;
import com.xebialabs.deployit.maven.packager.ManifestPackager;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Deploy artifacts to the target environment.
 *
 * @goal deploy
 * @phase pre-integration-test
 * @configurator override
 * @author Benoit Moussaud
 */

public class DeployMojo extends AbstractDeployitMojo {
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("deployit:deploy");
        if (environment == null)
            throw new MojoExecutionException("Environment is empty");

        startServer();

        ApplicationDeploymentPackager packager;
        if (useImportablePackage)
            packager = new ManifestPackager(artifactId, version, outputDirectory);
        else
            packager = new CliPackager(artifactId, version);

        getLog().info("create the main artifact");
        packager.addMavenArtifact(project.getArtifact());
        //Handle additionnal maven artifacts
        if (additionalArtifacts != null) {
            getLog().info("create the additional artifacts");
            for (int i = 0; i < additionalArtifacts.length; i++) {
                final Module additionalArtifact = additionalArtifacts[i];
                Artifact additional = getArtifact(additionalArtifact);
                packager.addMavenArtifact(additional);
            }
        }

        packager.perform();

        interpret(packager.getCliCommands());


        //Create Environment
        getLog().info("Create the environment");
        List<String> members = new ArrayList<String>();
        for (ConfigurationItem each : environment) {           
            interpret(each.getCli());
            if (each.isAddedToEnvironment())
                members.add(each.getLabel());
        }

        interpret("create Environment label=" + DEFAULT_ENVIRONMENT);
        for (String member : members) {
            interpret("modify " + DEFAULT_ENVIRONMENT + " members += \"" + member + "\" ");
        }

        if (middlewareResources != null) {
            getLog().info("create Middleware Resources");
            for (ConfigurationItem ci : middlewareResources) {
                interpret(ci.getCli());
                interpret("modify \"" + packager.getDeploymentPackageName() + "\" middlewareResources+=\"" + ci.getLabel() + "\"");
            }
        }



        if (commands != null) {
            getLog().info("Handle additional commands");
            for (String each : commands) {
                interpret(each);
            }
        }

        getLog().info("Create the Deployment");
        //String deploymentCmd="create Deployment label=currentDeploymement source=\\""${project.artifactId} - ${project.version}\\"" target=DefaultEnvironment";
        StringBuilder depCmd = new StringBuilder("create Deployment ");
        depCmd.append("label=" + DEFAULT_DEPLOYMENT).append(' ');
        depCmd.append("source=").append('"').append(packager.getDeploymentPackageName()).append('"').append(' ');
        depCmd.append("target=" + DEFAULT_ENVIRONMENT);
        interpret(depCmd.toString());

        if (mappings != null) {
            getLog().info("create Mappings");
            for (ConfigurationItem ci : mappings) {
                interpret(ci.getCli());
                interpret("modify \"" + DEFAULT_DEPLOYMENT + "\" mappings+=\"" + ci.getLabel() + "\"");
            }
        }

        //Go !
        interpret("changeplan steps");
        if (testmode) {
            interpret("deployit_nosteps");
            interpret("export");
        } else {
            interpret("deployit");
        }
        interpret("changeplan changes");

        getLog().info("end of deploy:deploy");
    }


    private Artifact getArtifact(final Module module)
            throws MojoExecutionException {
        getLog().debug("-translateIntoPath- " + module);
        String key = ArtifactUtils.versionlessKey(module.getGroupId(), module
                .getArtifactId());
        Artifact artifact = (Artifact) project.getArtifactMap().get(key);
        if (artifact == null) {
            throw new MojoExecutionException(
                    "The artifact "
                            + key
                            + " referenced in plugin as is not found the project dependencies");
        }
        return artifact;
    }

}
