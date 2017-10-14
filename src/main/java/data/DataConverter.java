package main.java.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 10/14/2017.
 */
public class DataConverter {

    public static final String TEXT = "TEXT";
    public static final String INT = "INT";
    public static final String DATE = "DATE";

    public static Map<String, Object> convertParams(Map<String, String[]> params,
                                                    Map<String,String> typeMapping){
        HashMap<String, Object> entry = new HashMap<>();
        for (String key:params.keySet()){
            String paramVal = params.get(key)[0];
            Object val = null;
            switch (typeMapping.get(key)){
                case TEXT:
                    val = convertTextParam(paramVal);
                    break;
                case INT:
                    val = convertIntParam(paramVal);
                    break;
                case DATE:
                    val = convertDateParam(paramVal);
                    break;
                default:
                    System.out.println(String.format("ERROR: Unknown param type: %s", typeMapping.get(key)));
            }
            if (val != null) {
                entry.put(key, val);
            }
        }
        return entry;
    }

    private static String convertTextParam(String val){
        return String.format("\'%s\'", val);
    }

    private static int convertIntParam(String val){
        return Integer.parseInt(val);
    }

    private static String convertDateParam(String val){
        return String.format("\'%s\'", val);
    }
}
