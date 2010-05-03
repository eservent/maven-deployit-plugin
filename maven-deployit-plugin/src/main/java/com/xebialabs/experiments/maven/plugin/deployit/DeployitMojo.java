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

import com.xebialabs.deployit.maven.AbstractDeployitMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Deploy the current artifact in one give environment.
 *
 * @goal deployit-install
 * @phase install
 * @configurator override
 */
@Deprecated
public class DeployitMojo extends AbstractDeployitMojo {


    public void execute() throws MojoExecutionException {
        throw new MojoExecutionException("Deprecated use the pre-integration-test instead !!");
    }


}
