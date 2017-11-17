package main.java.endpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.data.tables.pojos.Groups;
import main.java.util.Repository;
import main.java.util.RequestHelper;
import org.jooq.Record;
import org.jooq.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/1/2017.
 */
public class GroupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //First get the group Ids
        int userId = Integer.parseInt(request.getParameter("user_id"));
        List<Integer> groupIds = Repository.getDsl().select()
                .from(USER_GROUP_LINKS)
                .where(USER_GROUP_LINKS.USER_ID.eq(userId))
                .fetch(USER_GROUP_LINKS.GROUP_ID);

        //Then the groups
        List<Groups> groups = Repository.getDsl().selectFrom(GROUPS)
                .where(GROUPS.ID.in(groupIds))
                .fetchInto(Groups.class);

        Type listType = new TypeToken<List<Groups>>(){}.getType();
        response.getWriter().println(new Gson().toJson(groups, listType));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);

        //First Create the group
        String name = params.get("name");
        String description = params.get("description");
        OffsetDateTime timestampCreated = OffsetDateTime.parse(params.get("timestamp_created"));
        int user_id = Integer.parseInt(params.get("created_by"));
        Groups group = Repository.getDsl().insertInto(GROUPS, GROUPS.NAME, GROUPS.DESCRIPTION, GROUPS.TIMESTAMP_CREATED, GROUPS.CREATED_BY)
                .values(name, description, timestampCreated, user_id)
                .returning(GROUPS.ID).fetchOne().into(Groups.class);

        //Then create the group-user link
        UserGroupServlet.addUserToGroup(user_id, group.getId(), timestampCreated, true);
    }

    static List<String> getEmailAddressesByGroup(int groupId) throws ServletException {
        List<Integer> userIds = UserGroupServlet.getUserIdsByGroupId(groupId);
        return Repository.getDsl().selectFrom(USERS)
                .where(USERS.ID.in(userIds))
                .fetch(USERS.EMAIL);
    }

    public static Result<Record> getAllGroups() throws ServletException{
        return Repository.getDsl().select().from(GROUPS).fetch();
    }
}