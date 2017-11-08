package main.java.util;

import com.google.gson.Gson;
import org.jooq.Record;
import org.jooq.TableField;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class RequestHelper {
    public static Map<String, String> getBodyAsMap(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String postBody = buffer.toString();

        Gson g = new Gson();
        Map<String, String> bodyMap = g.fromJson(postBody, Map.class);
        return bodyMap;
    }

    public static void setResponseToMap(HttpServletResponse response, Map<String, String> bodyMap) throws IOException {
        Gson g = new Gson();
        String content = g.toJson(bodyMap, Map.class);
        response.getWriter().println(content);
    }

    public static void addFieldToMap(Map<String, String> fieldMap, TableField field, Record r){
        fieldMap.put(field.getName(), r.getValue(field).toString());
    }
}
