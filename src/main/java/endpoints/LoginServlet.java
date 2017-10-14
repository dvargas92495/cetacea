package main.java.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import main.java.data.DataConverter;
import main.java.data.Repository;

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
        Map<String, Object> entry = DataConverter.convertParams(request.getParameterMap(), userTypeMapping);
        Repository.createEntry(TABLE, entry);
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
