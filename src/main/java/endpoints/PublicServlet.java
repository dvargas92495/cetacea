package main.java.endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by David on 10/14/2017.
 */
public class PublicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/javascript;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String fileName = "client" + request.getRequestURI();
        String fileContent = new Scanner(new java.io.FileInputStream(fileName), "UTF-8").useDelimiter("\\A").next();
        response.getWriter().println(fileContent);
    }
}
