package main.java.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;
import main.java.data.DataConverter;
import main.java.data.Repository;
import main.java.util.RequestHelper;

/**
 * Created by David on 10/14/2017.
 *
 * TODO: Implement with FE
 */
public class LoginServlet extends HttpServlet {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String TABLE = "users";
    private static final Map<String, String> userTypeMapping = new HashMap<String, String>() {{
        put(FIRST_NAME, DataConverter.TEXT);
        put(LAST_NAME, DataConverter.TEXT);
        put(EMAIL, DataConverter.TEXT);
    }};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Map<String, Object> entry = DataConverter.convertParams(request.getParameterMap(), userTypeMapping);
//        Repository.createEntry(TABLE, entry);
        System.out.println("got this far");
        String CLIENT_ID = "548992550759-kmikahq1pkfhffgps85151j5o2a6gduu.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        Map<String, String> idTokenMap = RequestHelper.getBodyAsMap(request);
        String idTokenString = idTokenMap.get("idtoken");

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // Print user identifier
                String userId = payload.getSubject();
                System.out.println("User ID: " + userId);

                // Get profile information from payload
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                String locale = (String) payload.get("locale");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");
                System.out.println("Name: " + name);
                System.out.println("Family Name: " + familyName);
                System.out.println("Given Name: " + givenName);

            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (GeneralSecurityException e) {
            System.out.println("General security exception thrown.");
        }
    }


    protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
        return null;
    }


    protected String getRedirectUri(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        return null;
    }


    protected String getUserId(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        return null;
    }
}
