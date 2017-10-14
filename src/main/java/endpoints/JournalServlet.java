package main.java.endpoints;

import com.amazonaws.util.IOUtils;
import main.java.data.DataConverter;
import main.java.data.Repository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class JournalServlet extends HttpServlet{

    private static final String USER_ID = "user_id";
    private static final String ENTRY = "entry";
    private static final String TIMESTAMP = "timestamp";
    private static final String TABLE = "journals";
    private static final Map<String, String> journalTypeMapping = new HashMap<String, String>() {{
        put(USER_ID, DataConverter.INT);
        put(ENTRY, DataConverter.TEXT);
        put(TIMESTAMP, DataConverter.DATE);
    }};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> entry = DataConverter.convertParams(request.getParameterMap(), journalTypeMapping);
        Repository.createEntry(TABLE, entry);
    }
}
