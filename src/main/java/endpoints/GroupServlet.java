package main.java.endpoints;

import main.java.data.tables.records.GroupsRecord;
import main.java.util.Repository;
import main.java.util.RequestHelper;
import main.java.util.Scheduler;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Map;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/1/2017.
 */
public class GroupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        DSLContext create = Repository.getContext();

        //First Create the group
        String name = params.get("name");
        String description = params.get("description");
        OffsetDateTime timestampCreated = OffsetDateTime.parse(params.get("timestamp_created"));
        int user_id = Integer.parseInt(params.get("created_by"));
        GroupsRecord group = create.insertInto(GROUPS, GROUPS.NAME, GROUPS.DESCRIPTION, GROUPS.TIMESTAMP_CREATED, GROUPS.CREATED_BY)
                .values(name, description, timestampCreated, user_id)
                .returning(GROUPS.ID).fetch().get(0);

        //Then create the group-user link
        UserGroupServlet.addUserToGroup(user_id, group.getValue(GROUPS.ID), timestampCreated, true);
    }

    static ArrayList<String> getEmailAddressesByGroup(int groupId) throws ServletException {
        ArrayList<String> emails = new ArrayList<String>();
        DSLContext query = Repository.getContext();
        Result<Record> result = query.select().from(USERS).fetch();
        result.forEach(record -> emails.add(record.get(USERS.EMAIL)));
        return emails;
    }

    public static ArrayList<Integer> getAllGroupIds() throws ServletException{
        DSLContext create = Repository.getContext();
        ArrayList<Integer> groupIds = new ArrayList<>();
        create.select().from(GROUPS).fetch().forEach(r -> groupIds.add(r.get(GROUPS.ID)));
        return groupIds;
    }
}
