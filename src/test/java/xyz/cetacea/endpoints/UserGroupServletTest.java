package xyz.cetacea.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.queries.GroupsQueries;
import xyz.cetacea.queries.UserGroupLinksQueries;
import xyz.cetacea.queries.UsersQueries;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserGroupServletTest extends ServletTest {

    private static UserGroupServlet userGroupServlet;
    private static Groups group;

    BaseServlet getServlet() { return userGroupServlet; }

    @BeforeEach
    void setup() {
        userGroupServlet = new UserGroupServlet();
    }

    @Test
    void testGetAllUsersInGroup() throws ServletException {
        givenUser();
        givenGroup();
        givenUserInGroup();

        List<Users> users = userGroupServlet.getAllUsersInGroup(group.getId());
        assertEquals(1, users.size());
        assertTrue(users.contains(user));

        UserGroupLinksQueries.deleteUsersFromGroup(Collections.singletonList(user.getId()), group.getId());
        GroupsQueries.deleteGroup(group.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testAddUserToGroup() throws ServletException {
        givenUser();
        givenGroup();

        List<Users> users = userGroupServlet.addUserToGroup(group.getId(), user.getEmail(), OffsetDateTime.now(), false);
        assertEquals(1, users.size());
        assertTrue(users.contains(user));

        UserGroupLinksQueries.deleteUsersFromGroup(Collections.singletonList(user.getId()), group.getId());
        GroupsQueries.deleteGroup(group.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testDeleteUserFromGroup() throws ServletException {
        givenUser();
        givenGroup();
        givenUserInGroup();

        List<Users> users = userGroupServlet.deleteUserFromGroup(group.getId(), user.getId());
        assertEquals(0, users.size());

        GroupsQueries.deleteGroup(group.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    private static void givenGroup() throws ServletException {
        group = GroupsQueries.createGroup(GROUP_NAME, GROUP_DESCRIPTION, OffsetDateTime.now(), user.getId());
    }

    private static void givenUserInGroup() throws ServletException {
        UserGroupLinksQueries.addUserToGroup(user.getId(), group.getId(), Timestamp.valueOf(LocalDateTime.now()), false);
    }
}
