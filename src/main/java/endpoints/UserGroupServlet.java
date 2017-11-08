package main.java.endpoints;

import main.java.util.Repository;
import main.java.util.RequestHelper;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

import static main.java.data.Tables.*;

/**
 * Created by David on 11/7/2017.
 */
public class UserGroupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);

        int userId = Integer.parseInt(params.get("user_id"));
        int groupId = Integer.parseInt(params.get("group_id"));
        OffsetDateTime timestampCreated = OffsetDateTime.parse(params.get("timestamp_joined"));
        boolean isAdmin = Boolean.parseBoolean(params.getOrDefault("isAdmin", "false"));
        addUserToGroup(userId, groupId, timestampCreated, isAdmin);
    }

    static void addUserToGroup(int userId, int groupId, OffsetDateTime time, boolean isAdmin) throws ServletException {
        DSLContext create = Repository.getContext();
        create.insertInto(USER_GROUP_LINKS, USER_GROUP_LINKS.USER_ID, USER_GROUP_LINKS.GROUP_ID, USER_GROUP_LINKS.TIMESTAMP_JOINED, USER_GROUP_LINKS.IS_ADMIN)
                .values(userId, groupId, time, isAdmin)
                .execute();
    }
}
