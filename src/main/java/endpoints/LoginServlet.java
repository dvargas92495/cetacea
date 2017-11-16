package main.java.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import main.java.data.tables.pojos.Users;
import main.java.util.Repository;
import main.java.util.RequestHelper;

import static main.java.data.Tables.*;

/**
 * Created by David on 10/14/2017.
 *
 * TODO: Implement with FE
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("got this far");
        String CLIENT_ID = "548992550759-kmikahq1pkfhffgps85151j5o2a6gduu.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();


        Map<String, String> requestMap = RequestHelper.getBodyAsMap(request);
        String idTokenString = requestMap.get("idtoken");
        Boolean isSignup = Boolean.valueOf(requestMap.get("isSignup"));

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String lastName = (String) payload.get("family_name");
                String firstName = (String) payload.get("given_name");
                String userEmail = payload.getEmail();

                if (isSignup) {
                    Repository.getDsl().insertInto(USERS, USERS.FIRST_NAME, USERS.LAST_NAME, USERS.EMAIL, USERS.OAUTH_ID)
                            .values(firstName, lastName, userEmail, userId)
                            .execute();
                }

                Users checkUser = Repository.getDsl().selectFrom(USERS).where(USERS.OAUTH_ID.eq(userId)).fetchOne().into(Users.class);
                if (checkUser == null) {
                    System.out.println("Fucked up");
                }
                else {
                    response.getWriter().println(new Gson().toJson(checkUser, Users.class));
                }

            }
            else {
                System.out.println("Invalid ID Token");
            }

        } catch (GeneralSecurityException e) {
            System.out.println("General security exception thrown.");
        }

//        try {
//            GoogleIdToken idToken = verifier.verify(idTokenString);
//            if (idToken != null) {
//                Payload payload = idToken.getPayload();
//
//                // Print user identifier
//                String userId = payload.getSubject();
//
//                if (isSignup) {
//
//                }
//
//                else {
//                    Users checkUser = Repository.getDsl().selectFrom(USERS).where(USERS.OAUTH_ID.eq(userId)).fetchOne().into(Users.class);
//                    if (checkUser == null) {
//                        System.out.println("Fucked up");
//                    }
//                    else {
//                        response.getWriter().println(new Gson().toJson(checkUser, Users.class));
//                    }
//
////                    System.out.println("User ID: " + userId);
////
////                    // Get profile information from payload
////                    String email = payload.getEmail();
////                    boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
////                    String name = (String) payload.get("name");
////                    String pictureUrl = (String) payload.get("picture");
////                    String locale = (String) payload.get("locale");
////                    String familyName = (String) payload.get("family_name");
////                    String givenName = (String) payload.get("given_name");
////                    System.out.println("Name: " + name);
////                    System.out.println("Family Name: " + familyName);
////                    System.out.println("Given Name: " + givenName);
////                    }

//                } else {
//                    System.out.println("Invalid ID token.");
//                }
//        } catch (GeneralSecurityException e) {
//            System.out.println("General security exception thrown.");
//        }
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
