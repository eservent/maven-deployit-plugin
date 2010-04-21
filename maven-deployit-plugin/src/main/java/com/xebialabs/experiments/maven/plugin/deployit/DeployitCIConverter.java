package com.xebialabs.experiments.maven.plugin.deployit;

import com.xebialabs.deployit.BaseConfigurationItem;
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
              
    public DeployitCIConverter() {
        System.out.println("BMO IN CTOR");
    }
    public boolean canConvert(Class type) {
        System.out.println("BMO canConvert "+type) ;
        return BaseConfigurationItem.class.isAssignableFrom( type );
    }

    /**
     * @see org.codehaus.plexus.component.configurator.converters.ConfigurationConverter#fromConfiguration(org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup, org.codehaus.plexus.configuration.PlexusConfiguration, java.lang.Class, java.lang.Class, java.lang.ClassLoader, org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator, org.codehaus.plexus.component.configurator.ConfigurationListener)
     */
    public Object fromConfiguration( ConverterLookup converterLookup, PlexusConfiguration configuration, Class type,
                                     Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator,
                                     ConfigurationListener listener )
            throws ComponentConfigurationException
    {
        System.out.println("BMO fromConfiguration "+type) ;

        Object retValue = fromExpression( configuration, expressionEvaluator, type );

        System.out.println("BMO fromConfiguration  fromExp "+retValue) ;
        if ( retValue != null )
        {
            return retValue;
        }

        Class implementation = getClassForImplementationHint( type, configuration, classLoader );
        System.out.println("BMO fromConfiguration  implementation "+implementation) ;

        retValue = instantiateObject( implementation );
        System.out.println("BMO fromConfiguration  retValue from instantiateObject ["+retValue+"]");
        if (retValue != null ) {
            System.out.println("BMO fromConfiguration  retValue from instantiateObject ["+retValue.getClass()+"]");            
        }
        if ( !( retValue instanceof BaseConfigurationItem ) )
        {
            retValue = null;
        }

        //processConfiguration( (Target) retValue, configuration, expressionEvaluator );
        System.out.println("BMO fromConfiguration  final retValue ["+retValue+"]");
        return retValue;
    }

}
