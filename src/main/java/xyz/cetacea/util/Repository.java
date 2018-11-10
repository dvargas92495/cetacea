package xyz.cetacea.util;

import xyz.cetacea.Application;
import org.jooq.*;
import org.jooq.impl.DSL;

import javax.servlet.ServletException;
import java.util.function.Function;

/**
 * Created by David on 9/30/2017.
 */
public class Repository {

    public static String host = Application.DB_HOST;
    public static String user = Application.DB_USER;
    public static String password = Application.DB_PASSWORD;

    public static <T> T run(Function<DSLContext, T> action) throws ServletException{
        try {
            DSLContext r = DSL.using(host, user, password);
            T result = action.apply(r);
            r.close();
            return result;
        } catch (Exception ex) {
            System.out.println(String.format("ERROR: %s", ex.getMessage()));
            ex.printStackTrace();
            throw new ServletException(ex);
        }
    }
}
