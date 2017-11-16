package main.java.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
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
        Type mapType = new TypeToken<Map<String,String>>() {}.getType();
        return g.fromJson(postBody, mapType);
    }
}
