package xyz.cetacea.queries;

import xyz.cetacea.util.Repository;
import xyz.cetacea.data.tables.pojos.Groups;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import javax.servlet.ServletException;

import static xyz.cetacea.data.Tables.GROUPS;


public class GroupsQueries {

    public static Groups createGroup(String name, String description, OffsetDateTime timestampCreated, int userId) throws ServletException{
        return Repository.run((DSLContext r) ->
                r.insertInto(GROUPS, GROUPS.NAME, GROUPS.DESCRIPTION, GROUPS.TIMESTAMP_CREATED, GROUPS.CREATED_BY)
                        .values(name, description, Timestamp.valueOf(timestampCreated.toLocalDateTime()), userId)
                        .returning().fetchOne().into(Groups.class)
        );
    }

    public static List<Groups> getGroupInfoByGroupIds(List<Integer> groupIds) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.selectFrom(GROUPS)
             .where(GROUPS.ID.in(groupIds))
             .fetchInto(Groups.class)
        );

    }

    public static List<Groups> getAllGroups() throws ServletException{
        return Repository.run((DSLContext r) ->
                r.select().from(GROUPS).fetchInto(Groups.class)
        );
    }

    public static Groups updateGroup(String name, String description, int groupId) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.update(GROUPS)
             .set(GROUPS.NAME, name)
             .set(GROUPS.DESCRIPTION, description)
             .where(GROUPS.ID.eq(groupId))
             .returning().fetchOne().into(Groups.class)
        );
    }

    public static int deleteGroup(int groupId) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.deleteFrom(GROUPS).where(GROUPS.ID.eq(groupId)).execute()
        );
    }
}
