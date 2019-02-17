package xyz.cetacea.util;

public class Environment {

    public static final String PRODUCTION = "PRODUCTION";
    private static final String DEVELOPMENT = "DEVELOPMENT";

    public static final String ENVIRONMENT = getEnv("CETACEA_ENV", DEVELOPMENT);
    public static final int PORT = getEnv("PORT", 5000);

    public static final String MAIL_USER = getEnv("CETACEA_MAIL_USER", null);
    public static final String MAIL_PASSWORD = getEnv("CETACEA_MAIL_PASSWORD", null);
    public static final String MAIL_HOST = getEnv("CETACEA_MAIL_HOST", null);
    public static final int MAIL_PORT = getEnv("CETACEA_MAIL_PORT", 587);

    public static final String DB_USER = getEnv("CETACEA_DB_USER", null);
    public static final String DB_PASSWORD = "passwerd";// getEnv("CETACEA_DB_PASSWORD", null);
    public static final String DB_HOST = getEnv("CETACEA_DB_HOST", null);

    public static final String GOOGLE_CLIENT = getEnv("CETACEA_GOOGLE_CLIENT", null);

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
