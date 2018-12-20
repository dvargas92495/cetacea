package xyz.cetacea.queries;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.util.Repository;

import javax.servlet.ServletException;
import java.time.OffsetDateTime;
import java.util.Random;

public class QueriesTest {

    private static final Random random = new Random(DateTime.now().getMillis());
    private static final int MIN_VALUE = 48;
    private static final int MAX_VALUE = 122;

    static String FIRST_NAME = getRandomString(8);
    static String LAST_NAME = getRandomString(12);
    static String USER_EMAIL = getRandomString(16);
    static String OAUTH_ID = getRandomString(20);
    static String GROUP_NAME = getRandomString(8);
    static String GROUP_DESCRIPTION = getRandomString(16);
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

    static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(MAX_VALUE-MIN_VALUE)+MIN_VALUE);
            sb.append(c);
        }
        return sb.toString();
    }
}
