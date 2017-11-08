package main.java;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import main.java.endpoints.*;
import main.java.util.Scheduler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Timer;

public class Application {

    // Came with starter AWS, may use in future
    // Initialize the global AWS XRay Recorder with the Elastic Beanstalk plugin to trace environment metadata
    //static {
        //AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new ElasticBeanstalkPlugin());
        //AWSXRay.setGlobalRecorder(builder.build());
    //}

    public static final String PRODUCTION = "PRODUCTION";
    public static final String DEVELOPMENT = "DEVELOPMENT";

    private static int getPort() {
        String port = System.getenv().get("PORT") != null ? System.getenv().get("PORT"):"5000";
        return Integer.parseInt(port);
    }

    public static String getEnvironment() {
        return System.getenv().get("CETACEA_ENV") != null ? System.getenv().get("CETACEA_ENV"):DEVELOPMENT;
    }

    public static boolean isXRayEnabled() {
        return Boolean.valueOf(System.getenv("XRAY_ENABLED"));
    }

    public static void main(String[] args) throws Exception {

        //Establish Scheduler
        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        timer.schedule(
                new Scheduler(),
                date.getTime(),
                1000 * 60 * 60 * 24
        );

        //Establish Endpoints
        Server server = new Server(getPort());
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
        if (isXRayEnabled()) {
            FilterHolder filterHolder = handler.addFilterWithMapping(AWSXRayServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
            filterHolder.setInitParameter("dynamicNamingFallbackName", "ElasticBeanstalkSample");
            filterHolder.setInitParameter("dynamicNamingRecognizedHosts", "*");
        }
        server.setHandler(handler);
        server.start();
        server.join();
    }

}