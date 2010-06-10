package com.xebialabs.deployit.maven;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 29 avr. 2010
 * Time: 08:10:30
 * To change this template use File | Settings | File Templates.
 */
public class ManifestTest {

    @Test
    public void generateManifest() throws Exception {
        String artifactId = "artifact";
        String version = "1.2.2";
        Manifest m = new Manifest();
        final Attributes mainAttributes = m.getMainAttributes();
        mainAttributes.putValue("Manifest-Version", "1.0");
        mainAttributes.putValue("Deployit-Package-Format-Version", "1.0");
        mainAttributes.putValue("Deployit-Application", artifactId);
        mainAttributes.putValue("Deployit-Version", version);

        String packaging = "mypackaging";
        String ciType = "War";
        final Map<String, Attributes> entries = m.getEntries();
        Attributes attributes = new Attributes();
        attributes.putValue("Deployit-Type", packaging);
        attributes.putValue("Deployit-Name", artifactId + " - " + version);
        entries.put(ciType + "/" + "path-to-war", attributes);

        dumpManifest(m);
    }

    private void dumpManifest(Manifest m) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            m.write(baos);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println(new String(baos.toByteArray()));
    }
}
