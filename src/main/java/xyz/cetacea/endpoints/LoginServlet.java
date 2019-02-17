package xyz.cetacea.endpoints;

import javax.servlet.ServletException;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.eclipse.jetty.http.HttpMethod;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.queries.UsersQueries;
import xyz.cetacea.util.DependencyManager;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.util.IGoogleClient;
import xyz.cetacea.util.Param;

/**
 * Created by David on 10/14/2017.
 */
public class LoginServlet extends BaseServlet {
    private IGoogleClient googleClient;

    public LoginServlet() {
        googleClient = DependencyManager.getGoogleClient();
    }

    @Endpoint(HttpMethod.POST)
    public Users createUser(@Param("idtoken") String idTokenString) throws ServletException {
        Payload payload = getPayload(idTokenString);
        Users user = UsersQueries.getUserInfoByOAuth(payload.getSubject());
        if (user != null) {
            throw new RuntimeException(String.format("Could not create user %s because it already exists", user.getId()));
        }
        return UsersQueries.createUser(payload.get("given_name").toString(), payload.get("family_name").toString(), payload.getEmail(), payload.getSubject());
    }

    @Endpoint(HttpMethod.PUT)
    public Users loginUser(@Param("idtoken") String idTokenString) throws ServletException {
        Payload payload = getPayload(idTokenString);
        Users user = UsersQueries.getUserInfoByOAuth(payload.getSubject());
        if (user == null) {
            throw new RuntimeException(String.format("Could not login user %s because it doesn't exist", payload.getEmail()));
        }
        return user;
    }

    private Payload getPayload(String idTokenString) {
        try {
            return googleClient.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(String.format("Security Exception thrown when trying to verify: %s", idTokenString));
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException(String.format("Failed to parse: %s", idTokenString));
        }
    }
}
