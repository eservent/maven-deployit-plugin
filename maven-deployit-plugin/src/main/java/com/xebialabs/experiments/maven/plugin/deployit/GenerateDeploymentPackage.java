package com.xebialabs.experiments.maven.plugin.deployit;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.assembly.io.DefaultAssemblyReader;

import java.io.File;
import java.util.List;

/**
 * Export the
 *
 * @goal generate
 * @configurator override
 */

public class GenerateDeploymentPackage extends AbstractMojo {
    /**
     * @parameter default-value="${project.build.directory}"
     * @required
     * @readonly
     */
    private File outputDirectory;

    /**
     * @parameter default-value="${project.artifactId}"
     * @required
     * @readonly
     */
    private String artifactId;

    /**
     * @parameter default-value="${project.version}"
     * @required
     * @readonly
     */
    private String version;

    /**
     * @parameter default-value="${project.packaging}"
     * @required
     * @readonly
     */
    private String packaging;

    /**
     * @parameter default-value="${project.build.outputDirectory}/${project.build.finalName}.${project.packaging}"
     * @required
     */
    private File jeeArtifact;

    /**
     * the additionnal Deployments
     *
     * @parameter
     */
    protected Module[] additionnalArtifacts;

    DefaultAssemblyReader x;


    public void execute() throws MojoExecutionException, MojoFailureException {
        /*
          interpret("create Application label=\"" + artifactId + "\"");
        // XXXXXXX: passing artiactId as the label because the borked Interpreter will add the version
        String createDeploymentPackage = "create DeploymentPackage label=\"" + artifactId + "\" application=\"" + artifactId
                + "\" version=\"" + version + "\"";
        interpret(createDeploymentPackage);

        getLog().info("create the main artifact");
        deploymentPackageName = artifactId + " - " + version;
        createJeeArchiveCI(jeeArtifact, this.packaging);
        //Handle additionnal maven artifacts
        if (additionnalArtifacts != null) {
            getLog().info("create the additional artifacts");
            for (int i = 0; i < additionnalArtifacts.length; i++) {
                final Module additionnalArtifact = additionnalArtifacts[i];

                Artifact additional = getArtifact(additionnalArtifact);
                File additionalFile = additional.getFile();

                createJeeArchiveCI(additionalFile, additional.getType());
            }
        }
        */

    }
}
