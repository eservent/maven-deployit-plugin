package com.xebialabs.deployit.maven;

import com.xebia.ad.DeployItConfiguration;
import com.xebia.ad.ReleaseInfo;
import com.xebia.ad.Server;
import com.xebia.ad.cli.Interpreter;
import com.xebia.ad.setup.SetupDatabaseType;
import org.apache.maven.artifact.Artifact;
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
 * Provides common code for deployit mojos
 *
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
     * Activate the test mode, the steps are not executed.
     *
     * @parameter default-value=false
     */
    protected boolean testmode;


    /**
     * The main JEE artifact to deploy
     *
     * @parameter default-value="${project.build.directory}/${project.build.finalName}.${project.packaging}"
     * @required
     */
    protected File jeeArtifact;

    /**
     * Deployit Listen port
     *
     * @parameter default-value="8888" expression="${deployit.port}"
     */
    private int port;

    /**
     * Extra CLI commands.
     *
     * @parameter
     */
    protected String[] commands;

    /**
     * @parameter
     */
    protected List<ConfigurationItem> middlewareResources;


    /**
     * List of the Mapping
     *
     * @parameter
     */
    protected List<ConfigurationItem> mappings;

    /**
     * The target environment.
     *
     * @parameter
     */
    protected List<ConfigurationItem> environment;


    /**
     * Additional deployables artifacts
     * @parameter
     */
    protected List<DeployableArtifactItem> deployableArtifacts;


    /**
     * Tell if building a Deployit Importable Package (instead use direct cli interface)
     *
     * @parameter defaultvalue="false"
     */
    protected boolean useImportablePackage;


    private final StringBuffer fullScript = new StringBuffer();


    protected Interpreter interpreter;

    public static final String DEFAULT_ENVIRONMENT = "DefaultEnvironment";
    public static final String DEFAULT_DEPLOYMENT = "DefaultDeployment";

    private static boolean SERVER_STARTED = false;

    protected void startServer() {
        if (!SERVER_STARTED) {
            getLog().info("STARTING DEPLOYIT SERVER");
            DeployItConfiguration context = new DeployItConfiguration();

            context.setDatabaseType(SetupDatabaseType.HSQLDB);
            context.setDatabaseDriverClass(SetupDatabaseType.getDefaultDatabaseDriverClass(context.getDatabaseType()));
            context.setHibernateDialect(SetupDatabaseType.getHibernateDialect(context.getDatabaseType()));
            context.setDatabaseURL("jdbc:hsqldb:file:" + new File(outputDirectory, "./deployit.hdb").getPath()
                    + ";shutdown=true");
            context.setDatabaseUsername(SetupDatabaseType.getDefaultUsername(context.getDatabaseType()));
            context.setDatabasePassword("");
            File deployitRepoDir = new File(outputDirectory, "deployit.repo");
            deployitRepoDir.mkdir();
            context.setApplicationRepositoryPath(deployitRepoDir.getPath());
            context.setHttpPort(getPort());
            context.setApplicationToDeployPath("importablePackages");
            context.setMinThreads(10);
            context.setMaxThreads(50);
            context.setSecured(false);
            //Task extension only context.setExtensionsDirectoryPath("");

            context.save();

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ad-repository", context
                    .getCreationalJPAProperties());
            emf.close();

            final Server s = new Server(context, ReleaseInfo.getReleaseInfo());
            s.start();
            getLog().info("STARTED DEPLOYIT SERVER");
            SERVER_STARTED = true;
        }
    }

    public static void stopServer() {
        Server.shutdown();
        SERVER_STARTED = false;
    }

    protected void interpret(String line) throws MojoExecutionException {
        getLog().info("Interpret [" + line + "]");
        fullScript.append(line).append('\n');
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

    protected void deployit() throws MojoExecutionException {
        getLog().info(" ");
        getLog().info(" ");
        getLog().info("------------------------------------------------------------------");
        getLog().info("--- DEPLOYIT CHANGE PLAN  ----------------------------------------");
        getLog().info("------------------------------------------------------------------");
        interpret("changeplan steps");
        getLog().info("------------------------------------------------------------------");
        getLog().info("------------------------------------------------------------------");
        getLog().info("------------------------------------------------------------------");
        getLog().info(" ");
        getLog().info(" ");

        if (testmode) {
            interpret("deployit_nosteps");
            //interpret("export");
        } else {
            interpret("deployit");
        }
        interpret("changeplan changes");
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public boolean isTestmode() {
        return testmode;
    }

    public void setTestmode(boolean testmode) {
        this.testmode = testmode;
    }

    public File getJeeArtifact() {
        return jeeArtifact;
    }

    public void setJeeArtifact(File jeeArtifact) {
        this.jeeArtifact = jeeArtifact;
    }

    public int getPort() {
        return (port == 0 ? 8888 : port);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    public List<ConfigurationItem> getMiddlewareResources() {
        return middlewareResources;
    }

    public void setMiddlewareResources(List<ConfigurationItem> middlewareResources) {
        this.middlewareResources = middlewareResources;
    }

    public List<ConfigurationItem> getMappings() {
        return mappings;
    }

    public void setMappings(List<ConfigurationItem> mappings) {
        this.mappings = mappings;
    }

    public List<ConfigurationItem> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<ConfigurationItem> environment) {
        this.environment = environment;
    }

  

    public boolean isUseImportablePackage() {
        return useImportablePackage;
    }

    public void setUseImportablePackage(boolean useImportablePackage) {
        this.useImportablePackage = useImportablePackage;
    }

    public String getScript() {
        return fullScript.toString();
    }

    protected Artifact getArtifact(final DeployableArtifactItem item)
            throws MojoExecutionException {
        getLog().debug("-translateIntoPath- " + item.getLocation());
        String key = item.getLocation();
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
