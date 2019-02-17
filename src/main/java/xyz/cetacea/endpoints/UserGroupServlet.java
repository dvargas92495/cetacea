package xyz.cetacea.endpoints;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import org.eclipse.jetty.http.HttpMethod;
import xyz.cetacea.data.tables.pojos.UserGroupLinks;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.queries.UserGroupLinksQueries;
import xyz.cetacea.queries.UsersQueries;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.util.Param;
import xyz.cetacea.util.RequestHelper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 11/7/2017.
 */
public class UserGroupServlet extends BaseServlet {

    @Endpoint(HttpMethod.GET)
    public List<Users> getAllUsersInGroup(@Param("group_id") int groupId) throws ServletException {
        List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(groupId);
        return UsersQueries.getUserInfoByUserIds(userIds);
    }

    @Endpoint(HttpMethod.POST)
    public List<Users> addUserToGroup(@Param("group_id") int groupId, @Param("email") String email,
                                         @Param("timestamp_joined") OffsetDateTime timestampJoined,
                                         @Param("isAdmin") boolean isAdmin) throws ServletException {
        int userId = UsersQueries.getUserIdFromEmail(email);
        Timestamp timestamp = Timestamp.valueOf(timestampJoined.toLocalDateTime());
        UserGroupLinksQueries.addUserToGroup(userId, groupId, timestamp, isAdmin);
        return getAllUsersInGroup(groupId);
    }

    @Endpoint(HttpMethod.DELETE)
    public List<Users> deleteUserFromGroup(@Param("group_id") int groupId, @Param("user_id") int userId) throws ServletException {
        UserGroupLinksQueries.deleteUsersFromGroup(Collections.singletonList(userId), groupId);
        return getAllUsersInGroup(groupId);
    }
}
