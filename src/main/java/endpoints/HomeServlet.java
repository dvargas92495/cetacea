package main.java.endpoints;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

/**
 * Created by David on 10/14/2017.
 */
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getRequestURI();
        System.out.println("Home Getting: " + name);
        response.setStatus(HttpServletResponse.SC_OK);
        if (name.endsWith(".ico")){
            response.setContentType("image/x-icon;charset=utf-8");
            response.getOutputStream().println(loadFile(name));
        } else if (name.endsWith(".png")){
            String fileName = "client/public" + name;
            response.setContentType("image/png;charset=utf-8");
            File f = new File(fileName);
            BufferedImage bi = ImageIO.read(f);
            OutputStream out = response.getOutputStream();
            ImageIO.write(bi, "png", out);
            out.close();
        } else {
            name = "/index.html";
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(loadFile(name));
        }
    }

    static String loadFile(String name) {
        final String fileName = "client" + name;
        try {
            return new Scanner(new java.io.FileInputStream(fileName), "UTF-8").useDelimiter("\\A").next();
        } catch (final Exception exception) {
            return getStackTrace(exception);
        }
    }

    private static String getStackTrace(final Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter, true);
        throwable.printStackTrace(printWriter);

        return stringWriter.getBuffer().toString();
    }
}
