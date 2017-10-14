package main.java;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import main.java.endpoints.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Application {

    // Came with starter AWS, may use in future
    // Initialize the global AWS XRay Recorder with the Elastic Beanstalk plugin to trace environment metadata
    //static {
        //AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard().withPlugin(new ElasticBeanstalkPlugin());
        //AWSXRay.setGlobalRecorder(builder.build());
    //}

    private static int getPort() {
        String port = System.getenv().get("PORT") != null ? System.getenv().get("PORT"):"5000";
        return Integer.parseInt(port);
    }

    public static boolean isXRayEnabled() {
        return Boolean.valueOf(System.getenv("XRAY_ENABLED"));
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(getPort());
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HomeServlet.class, "/*");
        handler.addServletWithMapping(PublicServlet.class, "/public/*");
        handler.addServletWithMapping(TraceServlet.class, "/trace");
        handler.addServletWithMapping(CronServlet.class, "/crontask");
        handler.addServletWithMapping(EntryServlet.class, "/entry");
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