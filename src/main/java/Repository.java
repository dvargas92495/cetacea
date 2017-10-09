package main.java;

import java.sql.*;
import java.util.Map;

/**
 * Created by David on 9/30/2017.
 */
public class Repository {

    public void createJournalEntry(Map<String, Object> journalEntry){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO journals (");
        StringBuilder valSb = new StringBuilder();
        for (String column:journalEntry.keySet()){
            sb.append(column);
            sb.append(',');
            valSb.append(journalEntry.get(column));
            valSb.append(',');
        }
        sb.setLength(sb.length() - 1);
        valSb.setLength(valSb.length() - 1);
        sb.append(") VALUES (");
        sb.append(valSb);
        sb.append(");");
        query(sb.toString());
    }

    public void query(String q){
        Connection con = null;
        Statement st = null;
        int rs;


        String url = "jdbc:postgresql://aanlh5mrzrcgku.c2sjnb5f4d57.us-east-1.rds.amazonaws.com:5432/postgres";
        String user = "cetacea";
        String password = "passwerd";

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            st.executeUpdate(q);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(String.format("ERROR: %s", ex.getMessage()));
            ex.printStackTrace();
        } finally {
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
}
