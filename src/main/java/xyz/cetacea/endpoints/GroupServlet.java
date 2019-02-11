package xyz.cetacea.endpoints;

import org.eclipse.jetty.http.HttpMethod;
import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.UserGroupLinks;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.models.GroupPage;
import xyz.cetacea.models.GroupView;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.queries.GroupsQueries;
import xyz.cetacea.queries.UserGroupLinksQueries;
import xyz.cetacea.queries.UsersQueries;
import xyz.cetacea.util.Param;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David on 11/1/2017.
 */
public class GroupServlet extends BaseServlet {

    @Endpoint(HttpMethod.GET)
    public GroupPage getGroupsByUserId(@Param("user_id") int userId) throws ServletException {
        List<UserGroupLinks> userGroupLinksList = UserGroupLinksQueries.getUserGroupLinksByUserId(userId);
        List<Integer> groupIds = userGroupLinksList.stream().map(UserGroupLinks::getGroupId).collect(Collectors.toList());
        List<Groups> groups = GroupsQueries.getGroupInfoByGroupIds(groupIds);
        List<GroupView> groupViews = groups.stream().map(g -> {
            Optional<UserGroupLinks> link = userGroupLinksList.stream().filter(x -> x.getGroupId().equals(g.getId())).findFirst();
            return getGroupView(g, link.isPresent() && link.get().getIsAdmin());
        }).collect(Collectors.toList());

        return new GroupPage(groupViews);
    }

    @Endpoint(HttpMethod.PUT)
    public Groups updateGroupById(@Param("name") String name, @Param("description") String description, @Param("id") int id) throws ServletException {
        return GroupsQueries.updateGroup(name, description, id);
    }

    @Endpoint(HttpMethod.POST)
    public GroupView createGroup(@Param("name") String name, @Param("description") String description,
                                 @Param("timestamp_created") OffsetDateTime timestampCreated,
                                 @Param("created_by") int userId, @Param("members") String membersString) throws ServletException {
        Groups group = GroupsQueries.createGroup(name, description, timestampCreated, userId);

        //Then create the group-user link
        Timestamp timestamp = Timestamp.valueOf(timestampCreated.toLocalDateTime());
        UserGroupLinksQueries.addUserToGroup(userId, group.getId(), timestamp, true);

        List<String> members = memberStringsToList(membersString);
        List<Users> users = UsersQueries.getUsersByEmails(members);

        for(Users user : users){
            UserGroupLinksQueries.addUserToGroup(user.getId(), group.getId(), timestamp, false);
        }

        return getGroupView(group, true);
    }

    @Endpoint(HttpMethod.DELETE)
    public int deleteGroupById(@Param("id") int groupId) throws ServletException {
        List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(groupId);
        UserGroupLinksQueries.deleteUsersFromGroup(userIds, groupId);
        return GroupsQueries.deleteGroup(groupId);
    }

    private static GroupView getGroupView(Groups g, boolean isAdmin) {
        try {
            List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(g.getId());
            List<Users> members = UsersQueries.getUserInfoByUserIds(userIds);
            return new GroupView(g, members, isAdmin);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> memberStringsToList(String membersString) {
        String[] membersArr = membersString.split(",");

        List<String> cleanEmails = new ArrayList<>();
        for (String s : membersArr) {
            s = s.replaceAll("\\s", "");
            s = s.toLowerCase();
            cleanEmails.add(s);
        }

        return cleanEmails;
    }
}
