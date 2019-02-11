package xyz.cetacea.queries;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Users;

import javax.servlet.ServletException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsersQueriesTest extends QueriesTest {

    @BeforeEach
    void setup() throws ServletException {
        givenCreatedUser();
    }

    @Test
    void testCreateUser() {
        assertNotNull(createdUser, "Failed to create valid user entry.");
        verifyUser(createdUser);
    }

    @Test
    void testGetUserInfoByUserIds() throws ServletException {
        List<Users> usersInfo = UsersQueries.getUserInfoByUserIds(Collections.singletonList(createdUser.getId()));
        assertEquals(1, usersInfo.size(), "Could not find User by id: " + createdUser.getId());
        Users userInfo = usersInfo.get(0);
        verifyUser(userInfo);
        assertEquals(createdUser.getId(), userInfo.getId(), "User has wrong ID");
    }

    @Test
    void testGetEmailsByUserIds() throws ServletException {
        List<String> emails = UsersQueries.getEmailsByUserIds(Collections.singletonList(createdUser.getId()));
        assertEquals(1, emails.size(), "Could not find email by id: " + createdUser.getId());
        String email = emails.get(0);
        assertEquals(USER_EMAIL, email, "Wrong email returned");
    }

    @Test
    void testGetUserInfoByOAuth() throws ServletException {
        Users userInfo = UsersQueries.getUserInfoByOAuth(createdUser.getOauthId());
        assertNotNull(userInfo, "Could not find by oauth: " + createdUser.getOauthId());
        verifyUser(userInfo);
    }

    @Test
    void testGetUserIdFromEmail() throws ServletException {
        int userId = UsersQueries.getUserIdFromEmail(createdUser.getEmail());
        assertEquals((int)createdUser.getId(), userId, "Could not find id by email: " + createdUser.getEmail());
    }

    @Test
    void testGetUsersFromEmails() throws ServletException {
        List<Users> users = UsersQueries.getUsersByEmails(Collections.singletonList(createdUser.getEmail()));
        assertEquals(1, users.size());
        verifyUser(users.get(0));
    }

    @Test
    void testDeleteUserById() throws ServletException {
        int numDeletedUsers = UsersQueries.deleteUserById(createdUser.getId());
        assertEquals(1, numDeletedUsers);
    }

    @AfterEach
    void cleanup() throws ServletException {
        UsersQueries.deleteUserById(createdUser.getId());
    }

    private void verifyUser(Users user) {
        assertEquals(FIRST_NAME, user.getFirstName(), "User has wrong first name");
        assertEquals(LAST_NAME, user.getLastName(), "User has wrong last name");
        assertEquals(USER_EMAIL, user.getEmail(), "User has wrong email");
        assertEquals(OAUTH_ID, user.getOauthId(), "User has wrong oauth");
    }
}

