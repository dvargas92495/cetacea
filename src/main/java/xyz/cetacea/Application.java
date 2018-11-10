package xyz.cetacea;

import xyz.cetacea.endpoints.*;
import xyz.cetacea.util.Scheduler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Application {

    public static final String PRODUCTION = "PRODUCTION";
    public static final String DEVELOPMENT = "DEVELOPMENT";

    public static final String ENVIRONMENT = getEnv("CETACEA_ENV", DEVELOPMENT);
    public static final int PORT = getEnv("PORT", 5000);

    public static final String MAIL_USER = getEnv("CETACEA_MAIL_USER", null);
    public static final String MAIL_PASSWORD = getEnv("CETACEA_MAIL_PASSWORD", null);
    public static final String MAIL_HOST = getEnv("CETACEA_MAIL_HOST", null);
    public static final int MAIL_PORT = getEnv("CETACEA_MAIL_PORT", 587);

    public static final String DB_USER = getEnv("CETACEA_DB_USER", null);
    public static final String DB_PASSWORD = getEnv("CETACEA_DB_PASSWORD", null);
    public static final String DB_HOST = getEnv("CETACEA_DB_HOST", null);

    public static void main(String[] args) throws Exception {

        //Establish Scheduler
        Scheduler.init();

        //Establish Endpoints
        Server server = new Server(PORT);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(HomeServlet.class, "/*");
        handler.addServletWithMapping(PublicServlet.class, "/public/*");
        handler.addServletWithMapping(PublicServlet.class, "/node_modules/*");
        handler.addServletWithMapping(JournalServlet.class, "/api/journal");
        handler.addServletWithMapping(LoginServlet.class, "/api/login");
        handler.addServletWithMapping(EmailServlet.class, "/api/email");
        handler.addServletWithMapping(GroupServlet.class, "/api/group");
        handler.addServletWithMapping(UserGroupServlet.class, "/api/usergroup");

        server.setHandler(handler);
        server.start();
        server.join();
    }

    private static String getEnv(String envVariable, String defaultValue)
    {
        String val = System.getenv().get(envVariable);
        if (val == null)
        {
            return defaultValue;
        }
        return val;
    }

    private static int getEnv(String envVariable, int defaultValue)
    {
        return Integer.parseInt(getEnv(envVariable, String.valueOf(defaultValue)));
    }

}