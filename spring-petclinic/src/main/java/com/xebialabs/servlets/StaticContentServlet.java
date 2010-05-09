package com.xebialabs.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bmoussaud
 * Date: 9 mai 2010
 * Time: 10:28:56
 * To change this template use File | Settings | File Templates.
 */
public class StaticContentServlet extends HttpServlet{


    private  HttpServlet fileServlet = null;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doIt(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doIt(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void doIt(HttpServletRequest req, HttpServletResponse resp) {
        try {            
            getServletImplementation().service(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private synchronized HttpServlet getServletImplementation() {
        if (fileServlet != null)
            return fileServlet;

        try {
            System.out.println("Load \"weblogic.servlet.FileServlet\"");
            fileServlet = (HttpServlet) this.getClass().getClassLoader().loadClass("weblogic.servlet.FileServlet").newInstance();
            fileServlet.init(this.getServletConfig());
            return fileServlet;
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServletException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            System.out.println("Load \"org.apache.catalina.servlets.DefaultServlet\"");
            fileServlet = (HttpServlet) this.getClass().getClassLoader().loadClass("org.apache.catalina.servlets.DefaultServlet").newInstance();
            fileServlet.init(this.getServletConfig());
            return fileServlet;
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServletException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        throw new RuntimeException("Cannot find out a servlet implementation....");
    }
}
