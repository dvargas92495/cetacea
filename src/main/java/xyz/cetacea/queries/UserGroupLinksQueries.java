package xyz.cetacea.queries;

import xyz.cetacea.data.tables.pojos.UserGroupLinks;
import xyz.cetacea.util.Repository;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.util.List;

import static xyz.cetacea.data.Tables.USER_GROUP_LINKS;

public class UserGroupLinksQueries {

    public static UserGroupLinks addUserToGroup(int userId, int groupId, Timestamp timestamp, boolean isAdmin) throws ServletException {
        return Repository.run((DSLContext r) ->
                r.insertInto(USER_GROUP_LINKS, USER_GROUP_LINKS.USER_ID, USER_GROUP_LINKS.GROUP_ID, USER_GROUP_LINKS.TIMESTAMP_JOINED, USER_GROUP_LINKS.IS_ADMIN)
                        .values(userId, groupId, timestamp, isAdmin)
                        .returning().fetchOne().into(UserGroupLinks.class)
        );
    }

    public static List<UserGroupLinks> getUserGroupLinksByUserId(Integer userId) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.select()
             .from(USER_GROUP_LINKS)
             .where(USER_GROUP_LINKS.USER_ID.eq(userId))
             .fetchInto(UserGroupLinks.class)
        );
    }

    public static List<Integer> getUserIdsByGroupId(int groupId) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.selectFrom(USER_GROUP_LINKS)
             .where(USER_GROUP_LINKS.GROUP_ID.eq(groupId))
             .fetch(USER_GROUP_LINKS.USER_ID)
        );
    }

    public static int deleteUsersFromGroup(List<Integer> userIds, int groupId) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.deleteFrom(USER_GROUP_LINKS)
             .where(USER_GROUP_LINKS.USER_ID.in(userIds)).and(USER_GROUP_LINKS.GROUP_ID.eq(groupId))
             .execute()
        );
    }

}
