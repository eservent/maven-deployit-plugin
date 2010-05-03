package com.xebialabs.deployit.maven;

import com.xebia.ad.DeployItConfiguration;
import com.xebia.ad.ReleaseInfo;
import com.xebia.ad.Server;
import com.xebia.ad.cli.Interpreter;
import com.xebia.ad.setup.SetupDatabaseType;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.util.List;

/**
 * Provides common code for mojos
 * @author Benoit Moussaud
 */
public abstract class AbstractDeployitMojo extends AbstractMojo {
    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required @readonly
     */
    protected MavenProject project;


    /**
     * @parameter default-value="${project.build.directory}"
     * @required
     * @readonly
     */
    protected File outputDirectory;

    /**
     * @parameter default-value="${project.artifactId}"
     * @required
     * @readonly
     */
    protected String artifactId;

    /**
     * @parameter default-value="${project.version}"
     * @required
     * @readonly
     */
    protected String version;

    /**
     * @parameter default-value="${project.packaging}"
     * @required
     * @readonly
     */
    protected String packaging;

    /**
     * @parameter default-value=false
     */
    protected boolean testmode;


    /**
     * @parameter default-value="${project.build.directory}/${project.build.finalName}.${project.packaging}"
     * @required
     */
    protected File jeeArtifact;

    /**
     * @parameter default-value="8888" expression="${deployit.port}"
     */
    private int port;

    /**
     * @parameter
     */
    protected String[] commands;

    /**
     * @parameter
     */
    protected List<ConfigurationItem> middlewareResources;

    /**
     * @parameter
     */
    protected List<ConfigurationItem> environment;

    /**
     * the additionnal Deployments
     *
     * @parameter
     */
    protected Module[] additionalArtifacts;

    /**
     * Tell if building a Deployit Importable Package (instead use direct cli interface)
     *
     * @parameter defaultvalue="false"
     */
    protected boolean useImportablePackage;


    protected Interpreter interpreter;
    protected static final String DEFAULT_ENVIRONMENT = "DefaultEnvironment";
    protected static final String DEFAULT_DEPLOYMENT = "DefaultDeployment";

    private static boolean SERVER_STARTED = false;

    protected void startServer() {
        if (!SERVER_STARTED) {
            getLog().info("STARTING DEPLOYIT SERVER");
            DeployItConfiguration context = new DeployItConfiguration();

            context.setDatabaseType(SetupDatabaseType.HSQLDB);
            context.setDatabaseDriverClass(SetupDatabaseType.getDefaultDatabaseDriverClass(context.getDatabaseType()));
            context.setHibernateDialect(SetupDatabaseType.getHibernateDialect(context.getDatabaseType()));
            context.setDatabaseURL("jdbc:hsqldb:file:" + new File(outputDirectory, "/deployit.hdb").getPath()
                    + ";shutdown=true");
            context.setDatabaseUsername(SetupDatabaseType.getDefaultUsername(context.getDatabaseType()));
            context.setDatabasePassword("");
            File deployitRepoDir = new File(outputDirectory, "deployit.repo");
            deployitRepoDir.mkdir();
            context.setApplicationRepositoryPath(deployitRepoDir.getPath());
            context.setHttpPort(port);
            context.setApplicationToDeployPath("importablePackages");
            context.setMinThreads(10);
            context.setMaxThreads(50);
            context.save();

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ad-repository", context
                    .getCreationalJPAProperties());
            emf.close();

            ReleaseInfo info = ReleaseInfo.getReleaseInfo();
            final Server s = new Server(context, info);
            s.start();
            getLog().info("STARTED DEPLOYIT SERVER");
            SERVER_STARTED = true;
        }
    }

    protected void interpret(String line) throws MojoExecutionException {
        getLog().info("Interpret [" + line + "]");
        // getInterpreter().interpret(line);
        getInterpreter().interpretAndThrowExceptions(line);
    }

    protected void interpret(List<String> cliCommands) throws MojoExecutionException {
        for (String cmd : cliCommands)
            interpret(cmd);
    }

    protected Interpreter getInterpreter() throws MojoExecutionException {
        if (interpreter == null) {
            System.setProperty("cli.protocol", "http");
            ApplicationContext ctx = new ClassPathXmlApplicationContext(
                    new String[]{"/cli/unsecured/ad-cli-context.xml"});
            interpreter = (Interpreter) ctx.getBean("interpreter");
            if (interpreter == null) {
                throw new MojoExecutionException("Cannot find interpreter");
            }
            interpreter.afterPropertiesSet();
        }
        return interpreter;
    }
}
