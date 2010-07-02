package com.xebialabs.deployit.maven;

import com.xebialabs.deployit.maven.packager.ManifestPackager;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import java.io.File;

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

    /**
     * The Jar archiver.
     *
     * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="jar"
     */
    private JarArchiver jarArchiver;


    /**
     * The name of the EAR file to generate.
     *
     * @parameter alias="earName" expression="${project.build.finalName}"
     * @required
     */
    private String finalName;

    /**
     * Classifier to add to the artifact generated. If given, the artifact will
     * be an attachment instead.
     *
     * @parameter
     */
    private String classifier;


    /**
     * The archive configuration to use.
     * See <a href="http://maven.apache.org/shared/maven-archiver/index.html">Maven Archiver Reference</a>.
     *
     * @parameter
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;


    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("Build the Deployit Deployment Package");

        ManifestPackager packager = new ManifestPackager(artifactId, version, outputDirectory);

        packager.setGenerateManifestOnly(generateManifestOnly);
        getLog().info("Generate Deployment Package...");

        getLog().info("create the main artifact");
        packager.addMavenArtifact(project.getArtifact());
        //Handle additionnal maven artifacts
        if (deployableArtifacts != null) {
            getLog().info("create the additional artifacts");

            for (DeployableArtifactItem item : deployableArtifacts) {
                if (item.getLocation().contains(":")) {
                    getLog().info(" add a dependency deployable artifact " + item);
                    packager.addMavenArtifact(getArtifact(item));
                } else {
                    getLog().info(" add a deployable artifact " + item);
                    packager.addDeployableArtifact(item);
                }
            }

        }
        packager.perform();
        getLog().info("Manifest file generated in " + packager.getManifestFilePath());

        if (generateManifestOnly) {
            getLog().info("Do not seal the dar file, return");
            return;
        }

        try {

            File darFile = getDarFile(outputDirectory, finalName, classifier);
            getLog().info("Seal the archive in "+darFile);
            
            final MavenArchiver archiver = new MavenArchiver();
            final JarArchiver jarArchiver = getJarArchiver();
            getLog().debug("Jar archiver implementation[" + jarArchiver.getClass().getName() + "]");
            archiver.setArchiver(jarArchiver);
            archiver.setOutputFile(darFile);
            
            archiver.getArchiver().addDirectory(packager.getTargetDirectory());
            archiver.createArchive(getProject(), archive);

            if (classifier != null) {
                projectHelper.attachArtifact(getProject(), "dar", classifier, darFile);
            } else {
                getProject().getArtifact().setFile(darFile);
            }
        }
        catch (Exception e) {
            throw new MojoExecutionException("Error assembling DAR", e);
        }



    }


    /**
     * Returns the EAR file to generate, based on an optional classifier.
     *
     * @param basedir    the output directory
     * @param finalName  the name of the ear file
     * @param classifier an optional classifier
     * @return the EAR file to generate
     */
    private static File getDarFile(File basedir, String finalName, String classifier) {
        if (classifier == null) {
            classifier = "";
        } else if (classifier.trim().length() > 0 && !classifier.startsWith("-")) {
            classifier = "-" + classifier;
        }

        return new File(basedir, finalName + classifier + ".dar");
    }

    public JarArchiver getJarArchiver() {
        return jarArchiver;
    }
}
