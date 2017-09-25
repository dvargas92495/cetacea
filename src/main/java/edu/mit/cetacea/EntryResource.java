package main.java.edu.mit.cetacea;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by David on 9/25/2017.
 */
public class EntryResource extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(loadHtml());
    }

    private static String loadHtml() throws FileNotFoundException {
        final String fileName = "client/entry.html";
        return new Scanner(new java.io.FileInputStream(fileName), "UTF-8").useDelimiter("\\A").next();
    }
}
