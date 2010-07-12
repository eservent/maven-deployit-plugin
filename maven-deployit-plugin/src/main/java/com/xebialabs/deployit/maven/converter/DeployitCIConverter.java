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

package com.xebialabs.deployit.maven.converter;

import com.xebialabs.deployit.maven.ConfigurationItem;
import com.xebialabs.deployit.maven.MappingItem;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.ConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 21 avr. 2010
 * Time: 13:28:04
 * To change this template use File | Settings | File Templates.
 */
public class DeployitCIConverter extends AbstractConfigurationConverter {

    public static final String MAVEN_EXPRESSION_EVALUATOR_ID = "maven.expressionEvaluator";

    public static final String ROLE = ConfigurationConverter.class.getName();
    static final String TYPE = "type";
    static final String ADD_TO_ENV = "addToEnv";

    public boolean canConvert(Class type) {
        return ConfigurationItem.class.isAssignableFrom(type);
    }

    /**
     * @see org.codehaus.plexus.component.configurator.converters.ConfigurationConverter#fromConfiguration(org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup, org.codehaus.plexus.configuration.PlexusConfiguration, java.lang.Class, java.lang.Class, java.lang.ClassLoader, org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator, org.codehaus.plexus.component.configurator.ConfigurationListener)
     */
    public Object fromConfiguration(ConverterLookup converterLookup, PlexusConfiguration configuration, Class type,
                                    Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator,
                                    ConfigurationListener listener)
            throws ComponentConfigurationException {

        if (type.equals(ConfigurationItem.class)) {

            ConfigurationItem ci = new ConfigurationItem();

            try {
                ci.setMainType(configuration.getAttribute(TYPE));
                final String add2Env = configuration.getAttribute(ADD_TO_ENV);
                if (add2Env != null)
                    ci.setAddedToEnvironment(Boolean.parseBoolean(add2Env));
            } catch (Exception e) {
               throw new ComponentConfigurationException("getValue error",e);
            }

            for (PlexusConfiguration plexusConfiguration : configuration.getChildren()) {
                try {
                    String name = plexusConfiguration.getName();
                    String c = plexusConfiguration.getValue();
                    ci.addParameter(name, c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ci;
        }
        

        if (type.equals(MappingItem.class)) {

            MappingItem ci = new MappingItem();
            final PlexusConfiguration source = configuration.getChild("source");
            final PlexusConfiguration target = configuration.getChild("target");

            try {
                ci.setSource(source.getValue());
                ci.setTarget(target.getValue());
            } catch (PlexusConfigurationException e) {
               throw new ComponentConfigurationException("getValue error",e);
            }

            try {
                ci.setMainType(configuration.getAttribute(TYPE));
            } catch (Exception e) {
                e.printStackTrace();
            }
            PlexusConfiguration[] children = configuration.getChildren();

            for (PlexusConfiguration plexusConfiguration : children) {
                try {
                    String name = plexusConfiguration.getName();
                    String c = plexusConfiguration.getValue();
                    ci.addParameter(name, c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ci;
        }


        throw new ComponentConfigurationException("type not handled ("+type+")");


    }

}
