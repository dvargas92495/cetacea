package main.java.queries;

import main.java.data.tables.pojos.Users;
import main.java.util.Repository;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import java.rmi.server.ServerCloneException;
import java.util.List;

import static main.java.data.Tables.USERS;

public class UsersQueries {
    public static List<Users> getUserInfoByUserIds(List<Integer> userIds) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.ID.in(userIds))
             .fetchInto(Users.class)
        );
    }
    public static List<String> getEmailsByUserIds(List<Integer> userIds) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.ID.in(userIds))
             .fetch(USERS.EMAIL)
        );
    }

    public static Users getUserInfoByOAuth(String userId) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.OAUTH_ID.eq(userId))
             .fetchOneInto(Users.class)
        );
    }

    public static Users createUser(String firstName, String lastName, String userEmail, String userId) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.insertInto(USERS, USERS.FIRST_NAME, USERS.LAST_NAME, USERS.EMAIL, USERS.OAUTH_ID)
             .values(firstName, lastName, userEmail, userId)
             .returning().fetchOne().into(Users.class)
        );
    }

    public static Integer getUserIdFromEmail(String email) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.selectFrom(USERS)
             .where(USERS.EMAIL.eq(email))
             .fetchOne(USERS.ID)
        );
    }


}
