package main.java;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import main.java.endpoints.*;
import main.java.util.Scheduler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Application {

    // Came with starter AWS, may use in future
    // Initialize the global AWS XRay Recorder with the Elastic Beanstalk plugin to trace environment metadata
    //static {
        //AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new ElasticBeanstalkPlugin());
        //AWSXRay.setGlobalRecorder(builder.build());
    //}

    public static final String PRODUCTION = "PRODUCTION";
    public static final String DEVELOPMENT = "DEVELOPMENT";

    public static final String ENVIRONMENT = System.getenv().get("CETACEA_ENV") != null ? System.getenv().get("CETACEA_ENV"):DEVELOPMENT;
    public static final int PORT = System.getenv().get("PORT") != null ? Integer.parseInt(System.getenv().get("PORT")):5000;
    public static final boolean XRAY_ENABLED = Boolean.valueOf(System.getenv("XRAY_ENABLED"));

    public static final String MAIL_USER = System.getenv("CETACEA_MAIL_USER");
    public static final String MAIL_PASSWORD = System.getenv("CETACEA_MAIL_PASSWORD");
    public static final String MAIL_HOST = System.getenv("CETACEA_MAIL_HOST");
    public static final int MAIL_PORT = System.getenv().get("CETACEA_MAIL_PORT") != null ? Integer.parseInt(System.getenv().get("CETACEA_MAIL_PORT")):587;

    public static final String DB_USER = System.getenv("CETACEA_DB_USER");
    public static final String DB_PASSWORD = System.getenv("CETACEA_DB_PASSWORD");
    public static final String DB_HOST = System.getenv("CETACEA_DB_HOST");

    public static void main(String[] args) throws Exception {

        //Establish Scheduler
        Scheduler.init();

        //Establish Endpoints
        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HomeServlet.class, "/*");
        handler.addServletWithMapping(PublicServlet.class, "/public/*");
        handler.addServletWithMapping(PublicServlet.class, "/node_modules/*");
        handler.addServletWithMapping(TraceServlet.class, "/trace");
        handler.addServletWithMapping(JournalServlet.class, "/api/journal");
        handler.addServletWithMapping(LoginServlet.class, "/api/login");
        handler.addServletWithMapping(EmailServlet.class, "/api/email");
        handler.addServletWithMapping(GroupServlet.class, "/api/group");
        handler.addServletWithMapping(UserGroupServlet.class, "/api/usergroup");
        if (XRAY_ENABLED) {
            FilterHolder filterHolder = handler.addFilterWithMapping(AWSXRayServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
            filterHolder.setInitParameter("dynamicNamingFallbackName", "ElasticBeanstalkSample");
            filterHolder.setInitParameter("dynamicNamingRecognizedHosts", "*");
        }
        server.setHandler(handler);
        server.start();
        server.join();
    }

}