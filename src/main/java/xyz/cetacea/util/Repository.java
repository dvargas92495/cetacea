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

    public static <T> T run(Function<DSLContext, T> action) throws ServletException{
        try {
            DSLContext r = DSL.using(Application.DB_HOST, Application.DB_USER, Application.DB_PASSWORD);
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
