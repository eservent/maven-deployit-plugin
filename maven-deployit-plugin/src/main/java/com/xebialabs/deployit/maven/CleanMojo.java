package com.xebialabs.deployit.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Clean (Undeploy) the target environment.
 *
 * @goal clean
 * @phase post-integration-test
 * @configurator override
 * @author Benoit Moussaud
 */
public class CleanMojo extends AbstractDeployitMojo {
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("deployit:clean");

        startServer();

        interpret("delete " + DEFAULT_DEPLOYMENT);

        //Go !
        deployit();

        interpret("shutdown");
        
        getLog().info("end of deployit:clean");
    }
}
