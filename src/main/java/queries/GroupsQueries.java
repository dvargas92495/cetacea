package main.java.queries;

import main.java.util.Repository;
import main.java.data.tables.pojos.Groups;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import javax.servlet.ServletException;

import static main.java.data.Tables.GROUPS;


public class GroupsQueries {
    public static List<Groups> getGroupInfoByGroupIds(List<Integer> groupIds) throws ServletException{

        return Repository.getDsl().selectFrom(GROUPS)
                .where(GROUPS.ID.in(groupIds))
                .fetchInto(Groups.class);

    }
    public static void updateGroup(String name, String description, String groupId) throws ServletException{
        Repository.getDsl().update(GROUPS)
                .set(GROUPS.NAME, name)
                .set(GROUPS.DESCRIPTION, description)
                .where(GROUPS.ID.eq(Integer.parseInt(groupId)))
                .execute();
    }
    public static Groups createGroup(String name, String description, OffsetDateTime timestampCreated, int userId) throws ServletException{
        return Repository.getDsl().insertInto(GROUPS, GROUPS.NAME, GROUPS.DESCRIPTION, GROUPS.TIMESTAMP_CREATED, GROUPS.CREATED_BY)
                .values(name, description, Timestamp.valueOf(timestampCreated.toLocalDateTime()), userId)
                .returning(GROUPS.ID).fetchOne().into(Groups.class);

    }
    public static List<Groups> getAllGroups() throws ServletException{
        return Repository.getDsl().select().from(GROUPS).fetchInto(Groups.class);
    }
}
