package org.springframework.samples.petclinic.selenium;

import com.thoughtworks.selenium.DefaultSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 8 mai 2010
 * Time: 08:08:40
 * To change this template use File | Settings | File Templates.
 */
public class SeleniumTest {

    private DefaultSelenium selenium;

    @Before
    public void setUp() throws Exception {
        try {
            selenium = createSeleniumClient("http://localhost:8080/");
            selenium.start();
            System.out.println("Selenium Server handler is " + selenium);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }

    protected DefaultSelenium createSeleniumClient(String url) throws Exception {
        return new DefaultSelenium("localhost", 4444, "*firefox", url);
    }

    @Test
    public void testPetClinic() throws Exception {
        System.out.println("---PET CLINIC SELENIUM TEST ---");
        try {
            selenium.open("http://ubuntu-oss.local:8080/petclinic/");
            final String text = selenium.getText("//h2");
            //System.out.println("---- " + text);
            //Thread.sleep(1000 * 60);
            assertEquals("Welcome not found in the main page", "Welcome", text);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
            throw ex;
        }
        System.out.println("---/PET CLINIC SELENIUM TEST ---");
    }


}
