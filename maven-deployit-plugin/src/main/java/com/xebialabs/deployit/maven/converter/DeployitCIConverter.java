package com.xebialabs.deployit.maven.converter;

import com.xebialabs.deployit.maven.ConfigurationItem;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.ConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

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

        ConfigurationItem ci = new ConfigurationItem();

        try {
            ci.setMainType(configuration.getAttribute("type"));
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

}
