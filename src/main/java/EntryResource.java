package main.java;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.map.HashedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by David on 9/25/2017.
 */
public class EntryResource extends HttpServlet{

    Repository repo = new Repository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(loadHtml());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream in = request.getInputStream();
        String jsonString = IOUtils.toString(in);
        //TODO: HARD CODED HACK BELOW
        Map<String, Object> entry = new HashMap<>();
        //entry.put("id",5);
        entry.put("email","\'dvargas92495@gmail.com\'");
        entry.put("name","\'Vargas\'");
        entry.put("entry","\'Today was fantastic\'");
        entry.put("timestamp","\'" + (new Date()).toString() + "\'");
        repo.createJournalEntry(entry);
    }

    private static String loadHtml() throws FileNotFoundException {
        final String fileName = "client/entry.html";
        return new Scanner(new java.io.FileInputStream(fileName), "UTF-8").useDelimiter("\\A").next();
    }
}
