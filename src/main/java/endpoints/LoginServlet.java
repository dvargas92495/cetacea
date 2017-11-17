package main.java.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import main.java.data.tables.pojos.Users;
import main.java.util.Repository;
import main.java.util.RequestHelper;

import static main.java.data.Tables.*;

/**
 * Created by David on 10/14/2017.
 *
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

                Users userInfo = Repository.getDsl().selectFrom(USERS)
                        .where(USERS.OAUTH_ID.eq(userId))
                        .fetchOneInto(Users.class);

                if (isSignup && userInfo == null) {
                    userInfo = Repository.getDsl().insertInto(USERS, USERS.FIRST_NAME, USERS.LAST_NAME, USERS.EMAIL, USERS.OAUTH_ID)
                            .values(firstName, lastName, userEmail, userId)
                            .returning().fetchOne().into(Users.class);
                }
                if (userInfo == null) {
                    JsonObject resBody = new JsonObject();
                    resBody.addProperty("isAuthenticated", false);
                    resBody.addProperty("message", "Valid google account is not registered.");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().println(resBody.toString());
                }
                else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    JsonObject user = new Gson().toJsonTree(userInfo, Users.class).getAsJsonObject();
                    user.remove("oauthId");
                    user.addProperty("isAuthenticated", true);
                    response.getWriter().println(user.toString());
                }
            }
            else {
                JsonObject resBody = new JsonObject();
                resBody.addProperty("isAuthenticated", false);
                resBody.addProperty("message", "Invalid google account.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println(resBody.toString());
            }
        } catch (GeneralSecurityException e) {
            System.out.println("General security exception thrown.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal Server Error");
        }
    }
}
