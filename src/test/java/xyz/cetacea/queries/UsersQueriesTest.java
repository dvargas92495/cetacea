package xyz.cetacea.queries;

import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Users;

import javax.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsersQueriesTest extends QueriesTest {

    private static String FIRST_NAME = getRandomString(8);
    private static String LAST_NAME = getRandomString(12);
    private static String USER_EMAIL = getRandomString(16);
    private static String OAUTH_ID = getRandomString(20);

    @Test
    void testCreatedUser() throws ServletException {
        Users createdUser = UsersQueries.createUser(FIRST_NAME, LAST_NAME, USER_EMAIL, OAUTH_ID);
        assertNotNull(createdUser, "Failed to create valid user entry.");
        assertEquals(FIRST_NAME, createdUser.getFirstName());
        assertEquals(LAST_NAME, createdUser.getLastName());
        assertEquals(USER_EMAIL, createdUser.getEmail());
        assertEquals(OAUTH_ID, createdUser.getOauthId());
    }

}

