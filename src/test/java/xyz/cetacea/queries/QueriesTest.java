package xyz.cetacea.queries;

import org.junit.jupiter.api.BeforeAll;
import xyz.cetacea.CetaceaTest;
import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.util.Repository;

import javax.servlet.ServletException;
import java.time.OffsetDateTime;

public class QueriesTest extends CetaceaTest {

    static Users createdUser;
    static Groups createdGroup;

    @BeforeAll
    protected static void setupAll() {
        Repository.host = "jdbc:postgresql://localhost:5432/postgres";
        Repository.password = "";
    }

    void givenCreatedUser() throws ServletException {
        createdUser = UsersQueries.createUser(FIRST_NAME, LAST_NAME, USER_EMAIL, OAUTH_ID);
    }

    void givenCreatedGroup() throws ServletException {
        createdGroup = GroupsQueries.createGroup(GROUP_NAME, GROUP_DESCRIPTION, OffsetDateTime.now(), createdUser.getId());
    }
}
