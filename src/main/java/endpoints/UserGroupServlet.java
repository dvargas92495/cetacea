package main.java.endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import main.java.data.tables.pojos.Users;
import main.java.queries.UserGroupLinksQueries;
import main.java.queries.UsersQueries;
import main.java.util.RequestHelper;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 11/7/2017.
 */
public class UserGroupServlet extends HttpServlet {

    // get all users in a group
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int groupId = Integer.parseInt(request.getParameter("group_id"));

        List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(groupId);
        List<Users> users = UsersQueries.getUserInfoByUserIds(userIds);

        Type listType = new TypeToken<List<Users>>(){}.getType();
        JsonArray json = new Gson().toJsonTree(users, listType).getAsJsonArray();
        json.forEach(j -> j.getAsJsonObject().remove("oauthId"));
        response.getWriter().println(json.toString());

    }

    // add user to group
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        int groupId = Integer.parseInt(params.get("group_id"));

        String email = params.get("email");
        int userId = UsersQueries.getUserIdFromEmail(email);

        OffsetDateTime timestampJoined = OffsetDateTime.parse(params.get("timestamp_joined"));
        boolean isAdmin = Boolean.parseBoolean(params.getOrDefault("isAdmin", "false"));
        UserGroupLinksQueries.addUserToGroup(userId, groupId, timestampJoined, isAdmin);

        response.getWriter().println(getUsersAsJSON(groupId));


    }

    //remove user from group
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        int groupId = Integer.parseInt(params.get("group_id"));
        int userId = Integer.parseInt(params.get("user_id"));

        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(userId);
        UserGroupLinksQueries.deleteUsersFromGroup(userIdList, groupId);

        response.getWriter().println(getUsersAsJSON(groupId));
    }

    private static JSONArray getUsersAsJSON(int groupId) throws ServletException{
        List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(groupId);
        List<Users> users = UsersQueries.getUserInfoByUserIds(userIds);

        JSONArray userArray = new JSONArray();
        for(int j = 0; j < users.size(); j++){
            JSONObject newUser = new JSONObject();
            Users currentUser = users.get(j);
            newUser.put("id", currentUser.getId());
            newUser.put("firstName", currentUser.getFirstName());
            newUser.put("lastName", currentUser.getLastName());
            newUser.put("email", currentUser.getEmail());

            userArray.add(newUser);

        }
        return userArray;
    }

}
