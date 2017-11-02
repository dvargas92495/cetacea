package main.java.endpoints;

import main.java.util.Repository;
import main.java.util.Scheduler;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/1/2017.
 */
public class GroupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    static ArrayList<String> getEmailAddressesByGroup(int groupId) {
        ArrayList<String> emails = new ArrayList<String>();
        DSLContext create = Repository.getContext();
        if (create == null){
            return emails;
        }
        Result<Record> result = create.select().from(USERS).fetch();
        result.forEach(record -> emails.add(record.get(USERS.EMAIL)));
        return emails;
    }

    static ArrayList<Integer> getAllGroupIds(){
        ArrayList<Integer> groupsIds = new ArrayList<>();
        groupsIds.add(0); //TODO: Replace with db call
        return groupsIds;
    }
}
