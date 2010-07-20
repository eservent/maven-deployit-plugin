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

import com.xebialabs.deployit.maven.packager.ManifestPackager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.ArrayList;
import java.util.List;

/**
 * Deploy artifacts to the target environment.
 *
 * @author Benoit Moussaud
 * @goal deploy
 * @phase pre-integration-test
 * @configurator override
 */

public class DeployMojo extends AbstractDeployitMojo {
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("deployit:deploy");
		if (environment == null)
			throw new MojoExecutionException("Environment is empty");


		final ManifestPackager packager = new ManifestPackager(artifactId, version, outputDirectory);
		packager.setGenerateManifestOnly(generateManifestOnly);

		getLog().info("create the main artifact");
		packager.addDeployableArtifact(getRealDeployableArtifact(project.getArtifact()));

		//Handle additionnal maven artifacts
		if (deployableArtifacts != null) {
			getLog().info("create the additional artifacts");
			for (DeployableArtifactItem item : deployableArtifacts) {
				packager.addDeployableArtifact(getRealDeployableArtifact(item));
			}

		}


		packager.perform();

		startServer();		

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
			}
		}

		//Go !
		deployit();

		getLog().info("end of deploy:deploy");
	}


}
