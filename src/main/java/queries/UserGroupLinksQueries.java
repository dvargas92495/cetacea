package main.java.queries;

import com.sun.org.apache.xpath.internal.operations.Bool;
import main.java.data.tables.pojos.UserGroupLinks;
import main.java.util.Repository;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

import static main.java.data.Tables.USER_GROUP_LINKS;

public class UserGroupLinksQueries {
    public static List<UserGroupLinks> getUserGroupLinksByUserId(Integer userId) throws ServletException{
        return Repository.getDsl().select()
                .from(USER_GROUP_LINKS)
                .where(USER_GROUP_LINKS.USER_ID.eq(userId))
                .fetchInto(UserGroupLinks.class);
    }

    public static List<Integer> getUserIdsByGroupId(int groupId) throws ServletException{
        return Repository.getDsl().selectFrom(USER_GROUP_LINKS)
                .where(USER_GROUP_LINKS.GROUP_ID.eq(groupId))
                .fetch(USER_GROUP_LINKS.USER_ID);
    }

    public static void addUserToGroup(int userId, int groupId, OffsetDateTime time, boolean isAdmin) throws ServletException {
        Timestamp timestamp = Timestamp.valueOf(time.toLocalDateTime());
        Repository.getDsl().insertInto(USER_GROUP_LINKS, USER_GROUP_LINKS.USER_ID, USER_GROUP_LINKS.GROUP_ID, USER_GROUP_LINKS.TIMESTAMP_JOINED, USER_GROUP_LINKS.IS_ADMIN)
                .values(userId, groupId, timestamp, isAdmin)
                .execute();
    }

    public static void deleteUserFromGroup(int userId, int groupId) throws ServletException {
        Repository.getDsl().deleteFrom(USER_GROUP_LINKS)
                .where(USER_GROUP_LINKS.USER_ID.eq(userId)).and(USER_GROUP_LINKS.GROUP_ID.eq(groupId))
                .execute();
    }

}
