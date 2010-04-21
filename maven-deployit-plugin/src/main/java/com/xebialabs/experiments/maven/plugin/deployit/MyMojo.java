package com.xebialabs.experiments.maven.plugin.deployit;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.xebia.ad.DeployItConfiguration;
import com.xebia.ad.ReleaseInfo;
import com.xebia.ad.Server;
import com.xebia.ad.cli.Interpreter;
import com.xebia.ad.conversion.PropertyListConverter;
import com.xebia.ad.setup.SetupDatabaseType;
import com.xebialabs.deployit.BaseConfigurationItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.util.*;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;


/**
 * Goal which touches a timestamp file.
 * 
 * @goal deployit
 * 
 * @phase install
 * @configurator override
 */
public class MyMojo extends AbstractMojo {

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
	 * @parameter default-value=false
	 */
	private boolean testmode;


	/**
	 * @parameter default-value="${project.build.outputDirectory}/${project.build.finalName}.${project.packaging}"
	 * @required
	 */
	private File jeeArtifact;

	/**
	 * @parameter default-value="8080" expression="${deployit.port}"�
	 */
	private int port;

	/**
	 * @parameter
	 */
	private String[] commands;

	/**
	 * @parameter
	 */
	private List<BaseConfigurationItem> middlewareResources;
	
	/**
	 * @parameter
	 */
	private String[] environment;

    /**
     * the additionnal Deployments
     *
     * @parameter
     */
    protected Module[] additionnalArtifacts;

    
    private Interpreter interpreter;
    private static final String DEFAULT_ENVIRONMENT = "DefaultEnvironment";
    private static final String CURRENT_DEPLOYMENT = "DefaultDeployment";
    private String deploymentPackageName;

    private AbstractConfigurationConverter toto;

    public void execute() throws MojoExecutionException {
        getLog().info("Packaging "+this.packaging);

        
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
		
		System.setProperty("cli.protocol", "http");
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "/cli/unsecured/ad-cli-context.xml" });
        interpreter = (Interpreter) ctx.getBean("interpreter");
		if (interpreter == null) {
			throw new MojoExecutionException("Cannot find interpreter");
		}
		interpreter.afterPropertiesSet();



        interpret("create Application label=\"" + artifactId + "\"");
        // XXXXXXX: passing artiactId as the label because the borked Interpreter will add the version
		String createDeploymentPackage = "create DeploymentPackage label=\"" + artifactId + "\" application=\"" + artifactId
				+ "\" version=\"" + version + "\"";
        interpret(createDeploymentPackage);

        getLog().info("create the artifacts");
        deploymentPackageName = artifactId + " - " + version;
        createJeeArchiveCI(jeeArtifact,this.packaging);
          //Handle additionnal maven artifacts
        for (int i=0; i < additionnalArtifacts.length; i++) {
            final Module additionnalArtifact = additionnalArtifacts[i];

            Artifact additional = getArtifact(additionnalArtifact);
            File additionalFile = additional.getFile();

            createJeeArchiveCI(additionalFile, additional.getType());
        }

        // interpreter.interpret("import location=\"src/main/resources/" + packageName + "\"");
		/*
		for (BaseConfigurationItem it: environment) {
			System.out.println("--PL--> Found item env "+it);
			StringBuilder command = new StringBuilder();
			String label = it.getLabel();
			command.append("create ").append(it.getClass().getSimpleName()).append(" label=\"").append(label).append(
					"\" ");
			String convertedString = PropertyListConverter.convertObjectToString(it);
			command.append(convertedString);
			System.out.println("-PL  Env---------> "+command);
		}
		*/
        //Create Environment
        getLog().info("Create the environment");
		List<String> members = new ArrayList<String>();
		for (String each : environment) {
			members.add(extractLabel(each));
            interpret(each);
        }
        interpret("create Environment label=" + DEFAULT_ENVIRONMENT);
        for (String m : members) {
            interpret("modify " + DEFAULT_ENVIRONMENT + " members += \"" +m+"\" ");
        }
		

        getLog().info("create Middleware Resources");
		for (BaseConfigurationItem res : middlewareResources) {
			System.out.println("--PL--> Found a mr: \"" + res + "\"");
			String label = res.getLabel();
			StringBuilder command = new StringBuilder();
			command.append("create ").append(res.getClass().getSimpleName()).append(" label=\"").append(label).append(
					"\" ");
			String convertedString = PropertyListConverter.convertObjectToString(res);
			command.append(convertedString);
		    interpret(command.toString());
            interpret("modify \"" + deploymentPackageName + "\" middlewareResources+=\"" + label + "\"");
        }

		if (commands != null) {
            getLog().info("Handle additional commands");
			for (String each : commands) {
				System.out.println("-C---------> "+each);
                interpret(each);
            }
        }

        getLog().info("Create the Deployment");
		//String deploymentCmd="create Deployment label=currentDeploymement source=\\""${project.artifactId} - ${project.version}\\"" target=DefaultEnvironment";
		StringBuilder depCmd = new StringBuilder("create Deployment ");
		depCmd.append("label=" + CURRENT_DEPLOYMENT).append(' ');
		depCmd.append("source=").append('"').append(artifactId).append(" - ").append(version).append('"').append(' ');
		depCmd.append("target=" + DEFAULT_ENVIRONMENT);
        interpret(depCmd.toString());

        //Go !
        if (testmode) {
            interpret("changeplan steps");
            interpret("deployit_nosteps");
            interpret("export");
        }
        else {
            interpret("deployit");
        }
        
        interpret("shutdown");
    }

    private void createJeeArchiveCI(File archiveFile,  String type) {

        String ciType = null;
        if (type.compareToIgnoreCase("ear") == 0) {
            ciType = "Ear" ;
        }
        else if (type.compareToIgnoreCase("war") == 0) {
            ciType = "War" ;
        }
        else {
            getLog().warn("Not supported type ["+ciType+"], skit it");
            return;
        }

        String ciLabel = deploymentPackageName + "/"  +archiveFile.getName();

        interpret("create "+ciType+" label=\"" + ciLabel + "\" name=\""
                + FilenameUtils.getBaseName(archiveFile.getName()) + "\" location=\"" + archiveFile.getAbsolutePath() + "\"");
        interpret("modify \"" +  deploymentPackageName + "\" deployableArtifacts+=\"" + ciLabel + "\"");
    }

    private void interpret(String line) {
        getLog().info("Interpret ["+line+"]");
        interpreter.interpret(line);
    }

    //TODO: Handle spaces in the label name
    private String extractLabel(String each) {
		String[] split = each.split(" ");
		for (int i = 0; i < split.length; i++) {
			String prop = split[i];
			if (prop.startsWith("label")) {
				return prop.substring(prop.indexOf('=')+1).replace('"',' ').trim();
			}
		}
		return null;
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
