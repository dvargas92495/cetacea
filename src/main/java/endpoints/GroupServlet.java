package main.java.endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import main.java.data.tables.pojos.Groups;
import main.java.data.tables.pojos.UserGroupLinks;
import main.java.data.tables.pojos.Users;
import main.java.queries.GroupsQueries;
import main.java.queries.UserGroupLinksQueries;
import main.java.queries.UsersQueries;
import main.java.util.Repository;
import main.java.util.RequestHelper;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/1/2017.
 */
public class GroupServlet extends HttpServlet {

    //get all groups for user
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("user_id"));

        // get group ids and is_admin for user_id
        List<UserGroupLinks> userGroupLinksList = UserGroupLinksQueries.getUserGroupLinksByUserId(userId);

        // create just group ids
        List<Integer> groupIds = userGroupLinksList.stream().map( x -> x.getGroupId()).collect(Collectors.toList());

        // get group information
        List<Groups> groups = GroupsQueries.getGroupInfoByGroupIds(groupIds);


        // create json object
        JSONObject fullGroups = new JSONObject();


        for(int i = 0; i < groups.size(); i++){
            int currentGroupId = groups.get(i).getId();
            List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(currentGroupId);
            List<Users> users = UsersQueries.getUserInfoByUserIds(userIds);

            JSONObject newEntry = new JSONObject();

            Boolean isAdmin = userGroupLinksList.stream().filter(x -> x.getGroupId().equals(currentGroupId)).findFirst().get().getIsAdmin();
            newEntry.put("isAdmin", isAdmin);
            newEntry.put("name", groups.get(i).getName());
            newEntry.put("description", groups.get(i).getDescription());

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
            newEntry.put("members", userArray);

            fullGroups.put(currentGroupId, newEntry);

        }

        response.getWriter().println(fullGroups);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);

        //First Create the group
        String name = params.get("name");
        String description = params.get("description");
        String groupId = params.get("id");
        if (groupId != null){
            GroupsQueries.updateGroup(name, description, groupId);
        } else {
            OffsetDateTime timestampCreated = OffsetDateTime.parse(params.get("timestamp_created"));
            int userId = Integer.parseInt(params.get("created_by")); // user id for cetacea

            Groups group = GroupsQueries.createGroup(name, description, timestampCreated, userId);

            //Then create the group-user link
            UserGroupLinksQueries.addUserToGroup(userId, group.getId(), timestampCreated, true);

            String membersString = params.get("members");
            List<String> members = memberStringsToList(membersString);
            List<Users> users = EmailServlet.getUserIdFromEmail(members);

            for(Users user : users){
                UserGroupLinksQueries.addUserToGroup(user.getId(), group.getId(), timestampCreated, false);
            }

        }
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
