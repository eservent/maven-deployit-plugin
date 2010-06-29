package com.xebialabs.deployit.maven;

import com.xebialabs.deployit.maven.packager.ManifestPackager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Build up the Deployit Deployment Package
 *
 * @author Benoit Moussaud
 * @phase package
 * @goal generate-deployment-package
 * @requiresDependencyResolution compile
 * @configurator override
 */
public class GenerateDeploymentPackageMojo extends AbstractDeployitMojo {

    /**
     * Only the Manifest file will be generate. Do not copy files when generating Deployment package
     *
     * @parameter default-value=false
     */
    protected boolean generateManifestOnly;

    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("Build the Deployit Deployment Package");

        ManifestPackager packager = new ManifestPackager(artifactId, version, outputDirectory);

        packager.setGenerateManifestOnly(generateManifestOnly);
        getLog().info("Generate Deployment Package...");

        getLog().info(" create the main artifact");
        packager.addMavenArtifact(project.getArtifact());
         //Handle additionnal maven artifacts
        if (deployableArtifacts != null) {
            getLog().info("create the additional artifacts");

            for (DeployableArtifactItem item : deployableArtifacts) {
                if (item.getLocation().contains(":"))
                    packager.addMavenArtifact(getArtifact(item));
                else
                    packager.addDeployableArtifact(item);
            }

        }
        packager.perform();

        getLog().info("Manifest file generated in " + packager.getManifestFilePath());
    }
}
