package xyz.cetacea.queries;

import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.util.Repository;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import java.util.List;

import static xyz.cetacea.data.Tables.USERS;

public class UsersQueries {

    public static Users createUser(String firstName, String lastName, String userEmail, String oauthId) throws ServletException {
        return Repository.run((DSLContext r) ->
                r.insertInto(USERS, USERS.FIRST_NAME, USERS.LAST_NAME, USERS.EMAIL, USERS.OAUTH_ID)
                        .values(firstName, lastName, userEmail, oauthId)
                        .returning().fetchOne().into(Users.class)
        );
    }

    public static List<Users> getUserInfoByUserIds(List<Integer> userIds) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.ID.in(userIds))
             .fetchInto(Users.class)
        );
    }

    public static List<String> getEmailsByUserIds(List<Integer> userIds) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.ID.in(userIds))
             .fetch(USERS.EMAIL)
        );
    }

    public static Users getUserInfoByOAuth(String oauthId) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.OAUTH_ID.eq(oauthId))
             .fetchOneInto(Users.class)
        );
    }

    public static Integer getUserIdFromEmail(String email) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.EMAIL.eq(email))
             .fetchOne(USERS.ID)
        );
    }

    public static int deleteUserById(int id) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.deleteFrom(USERS)
             .where(USERS.ID.eq(id))
             .execute()
        );
    }
}
