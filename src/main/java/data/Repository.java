package main.java.data;

import org.postgresql.core.Field;
import org.postgresql.jdbc.PgResultSet;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 9/30/2017.
 */
public class Repository {

    public static void createEntry(String table, Map<String, Object> entry){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("INSERT INTO %s (",table));
        StringBuilder valSb = new StringBuilder();
        for (String column:entry.keySet()){
            sb.append(column);
            sb.append(',');
            valSb.append(entry.get(column));
            valSb.append(',');
        }
        sb.setLength(sb.length() - 1);
        valSb.setLength(valSb.length() - 1);
        sb.append(") VALUES (");
        sb.append(valSb);
        sb.append(");");
        update(sb.toString());
    }

    public static List<Map<String, Object>> queryEntry(String table, Map<String, Object> filters){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT * FROM %s WHERE ",table));
        for (String column:filters.keySet()){
            sb.append(column);
            sb.append(" = ");
            sb.append(filters.get(column));
            sb.append(" AND ");
        }
        sb.setLength(sb.length() - 5);
        sb.append(';');
        return query(sb.toString());
    }

    private static void update(String q){

        Connection con = null;
        Statement st = null;

        try {
            con = getConnection();
            st = con.createStatement();
            st.executeUpdate(q);
        } catch (SQLException | ClassNotFoundException ex) {
            error(ex);
        } finally {
            finish(con, st);
        }
    }

    private static List<Map<String, Object>> query(String q) {
        Connection con = null;
        Statement st = null;
        List<Map<String, Object>> rows = new ArrayList<>();

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(q);
            while (rs.next()){
                Map<String, Object> row = new HashMap<>();

                rows.add(row);
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException ex) {
            error(ex);
        } finally {
            finish(con, st);
        }
        return rows;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException{
        String url = "jdbc:postgresql://aanlh5mrzrcgku.c2sjnb5f4d57.us-east-1.rds.amazonaws.com:5432/postgres";
        String user = "cetacea";
        String password = "passwerd";

        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    private static void error(Exception ex){
        System.out.println(String.format("ERROR: %s", ex.getMessage()));
        ex.printStackTrace();
    }

    private static void finish(Connection con, Statement st) {
        try {
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            System.out.println(String.format("WARNING: %s", ex.getMessage()));
            ex.printStackTrace();
        }
    }
}
