package xyz.cetacea.util;

public class DependencyManager {
    private static IGoogleClient googleClient = new GoogleClient();
    public static IGoogleClient getGoogleClient() { return googleClient; }
    public static void setGoogleClient(IGoogleClient iGoogleClient) { googleClient = iGoogleClient; }
}
