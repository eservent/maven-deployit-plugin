package com.xebialabs.deployit.maven;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 28 juin 2010
 * Time: 14:20:19
 * To change this template use File | Settings | File Templates.
 */
public class MappingItem extends ConfigurationItem {

    private String source;
    private String target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getLabel() {
      return '"'+source + " to "+ target +" for " + DeployMojo.DEFAULT_DEPLOYMENT+'"';
    }

    @Override
    public void addParameter(String name, Object value) {
        if ("source".equals(name) )
            return;

        if ("target".equals(name) )
            return;

        if ("label".equals(name) )
            return;

        super.addParameter(name, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public String getCli() {
        return "modify "+getLabel()+ " "+parameters;
    }
}
