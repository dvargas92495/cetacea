package main.java.endpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.data.tables.pojos.Groups;
import main.java.data.tables.pojos.Users;
import main.java.util.Repository;
import main.java.util.RequestHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/1/2017.
 */
public class GroupServlet extends HttpServlet {

    //get all groups for user
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
        String groupId = params.get("id");
        if (groupId != null){
            Repository.getDsl().update(GROUPS)
                    .set(GROUPS.NAME, name)
                    .set(GROUPS.DESCRIPTION, description)
                    .where(GROUPS.ID.eq(Integer.parseInt(groupId)))
                    .execute();
        } else {
            OffsetDateTime timestampCreated = OffsetDateTime.parse(params.get("timestamp_created"));
            int userId = Integer.parseInt(params.get("created_by")); // user id for cetacea
            Groups group = Repository.getDsl().insertInto(GROUPS, GROUPS.NAME, GROUPS.DESCRIPTION, GROUPS.TIMESTAMP_CREATED, GROUPS.CREATED_BY)
                    .values(name, description, Timestamp.valueOf(timestampCreated.toLocalDateTime()), userId)
                    .returning(GROUPS.ID).fetchOne().into(Groups.class);

            //Then create the group-user link
            UserGroupServlet.addUserToGroup(userId, group.getId(), timestampCreated, true);

            String membersString = params.get("members");
            List<String> members = memberStringsToList(membersString);
            List<Users> users = EmailServlet.getUserIdFromEmail(members);

            for(Users user : users){
                UserGroupServlet.addUserToGroup(user.getId(), group.getId(), timestampCreated, false);
            }

        }
    }

    static List<String> getEmailAddressesByGroup(int groupId) throws ServletException {
        List<Integer> userIds = UserGroupServlet.getUserIdsByGroupId(groupId);
        return Repository.getDsl().selectFrom(USERS)
                .where(USERS.ID.in(userIds))
                .fetch(USERS.EMAIL);
    }

    public static List<Groups> getAllGroups() throws ServletException{
        return Repository.getDsl().select().from(GROUPS).fetchInto(Groups.class);
    }

    static List<String> memberStringsToList(String membersString) throws ServletException {
        String[] membersArr = membersString.split(",");

        List<String> cleanEmails = new ArrayList();
        for (String s : membersArr) {
            s = s.replaceAll("\\s", "");
            s.toLowerCase();
            cleanEmails.add(s);
        }

        return cleanEmails;
    }

    public static List<Groups> getDevGroups() throws ServletException{
        return new ArrayList<>();
    }
}
