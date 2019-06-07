package xyz.cetacea.util;

import org.jooq.*;
import org.jooq.impl.DSL;

import javax.servlet.ServletException;
import java.util.function.Function;

/**
 * Created by David on 9/30/2017.
 */
public class Repository {

    public static String host = Environment.isProd() ? Environment.DB_HOST : "jdbc:postgresql://localhost:5432/postgres";
    public static String user = Environment.isProd() ? Environment.DB_USER : "cetacea";
    public static String password = Environment.isProd() ? Environment.DB_PASSWORD : "";

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
