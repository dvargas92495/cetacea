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
        response.setStatus(HttpServletResponse.SC_OK);
        String name = request.getRequestURI();
        System.out.println("Public Getting: " + name);
        if (name.endsWith(".js")){
            response.setContentType("text/javascript;charset=utf-8");
        } else if (name.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (name.endsWith(".woff")) {
            response.setContentType("application/font-woff;charset=utf-8");
        } else if (name.endsWith(".ttf")) {
            response.setContentType("application/octet-stream;charset=utf-8");
        }  else if (name.endsWith(".css.map")) {
            response.setContentType("application/json;charset=utf-8");
        }
        response.getWriter().println(HomeServlet.loadFile(name));
    }
}
