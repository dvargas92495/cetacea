package main.java.endpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.data.tables.pojos.Users;
import main.java.util.Repository;
import main.java.util.RequestHelper;

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
 * Created by David on 11/7/2017.
 */
public class UserGroupServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int groupId = Integer.parseInt(request.getParameter("group_id"));
        List<Integer> userIds = getUserIdsByGroupId(groupId);
        List<Users> users = Repository.getDsl().selectFrom(USERS)
                .where(USERS.ID.in(userIds))
                .fetchInto(Users.class);
        Type listType = new TypeToken<List<Users>>(){}.getType();
        response.getWriter().println(new Gson().toJson(users, listType));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);

        int userId = Integer.parseInt(params.get("user_id"));
        int groupId = Integer.parseInt(params.get("group_id"));
        OffsetDateTime timestampJoined = OffsetDateTime.parse(params.get("timestamp_joined"));
        boolean isAdmin = Boolean.parseBoolean(params.getOrDefault("isAdmin", "false"));
        addUserToGroup(userId, groupId, timestampJoined, isAdmin);
    }

    static void addUserToGroup(int userId, int groupId, OffsetDateTime time, boolean isAdmin) throws ServletException {
        Repository.getDsl().insertInto(USER_GROUP_LINKS, USER_GROUP_LINKS.USER_ID, USER_GROUP_LINKS.GROUP_ID, USER_GROUP_LINKS.TIMESTAMP_JOINED, USER_GROUP_LINKS.IS_ADMIN)
                .values(userId, groupId, time, isAdmin)
                .execute();
    }

    static List<Integer> getUserIdsByGroupId(int groupId) throws ServletException{
        return Repository.getDsl().selectFrom(USER_GROUP_LINKS)
                .where(USER_GROUP_LINKS.GROUP_ID.eq(groupId))
                .fetch(USER_GROUP_LINKS.USER_ID);
    }
}
