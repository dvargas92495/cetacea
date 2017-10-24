package main.java.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;

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
        //Map<String, Object> entry = DataConverter.convertParams(request.getParameterMap(), userTypeMapping);
        //Repository.createEntry(TABLE, entry);
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
