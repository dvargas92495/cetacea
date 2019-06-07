package xyz.cetacea;

import xyz.cetacea.endpoints.*;
import xyz.cetacea.util.DependencyManager;
import xyz.cetacea.util.Environment;
import xyz.cetacea.util.MockGoogleClient;
import xyz.cetacea.util.Scheduler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Application {
    public static void main(String[] args) throws Exception {

        //Establish Scheduler
        // Scheduler.init();
        if(!Environment.isProd()) {
            DependencyManager.setGoogleClient(new MockGoogleClient());
        }

        //Establish Endpoints
        Server server = new Server(Environment.PORT);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HomeServlet.class, "/*");
        handler.addServletWithMapping(PublicServlet.class, "/public/*");
        handler.addServletWithMapping(PublicServlet.class, "/node_modules/*");
        handler.addServletWithMapping(JournalServlet.class, "/api/journal");
        handler.addServletWithMapping(LoginServlet.class, "/api/login");
        handler.addServletWithMapping(GroupServlet.class, "/api/group");
        handler.addServletWithMapping(UserGroupServlet.class, "/api/usergroup");

        server.setHandler(handler);
        server.start();
        server.join();
    }
}