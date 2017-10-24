package main.java;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.*;

/**
 * Created by David on 9/30/2017.
 */
public class Repository {

    public static DSLContext getContext() {
        String url = "jdbc:postgresql://aanlh5mrzrcgku.c2sjnb5f4d57.us-east-1.rds.amazonaws.com:5432/postgres";
        String user = "cetacea";
        String password = "passwerd";
        try {
            return DSL.using(url, user, password);
        } catch (Exception ex) {
            System.out.println(String.format("ERROR: %s", ex.getMessage()));
            ex.printStackTrace();
            return null;
        }
    }
}
