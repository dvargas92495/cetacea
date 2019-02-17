package xyz.cetacea.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Groups;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.models.GroupPage;
import xyz.cetacea.models.GroupView;
import xyz.cetacea.queries.GroupsQueries;
import xyz.cetacea.queries.UserGroupLinksQueries;
import xyz.cetacea.queries.UsersQueries;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GroupServletTest extends ServletTest {

    private static GroupServlet groupServlet;
    private static Groups group;

    BaseServlet getServlet() { return groupServlet; }

    @BeforeEach
    void setup() {
        groupServlet = new GroupServlet();
    }

    @Test
    void testGetGroupsByUserId() throws ServletException {
        givenUser();
        givenGroup();
        UserGroupLinksQueries.addUserToGroup(user.getId(), group.getId(), Timestamp.valueOf(LocalDateTime.now()), true);

        GroupPage groupPage = groupServlet.getGroupsByUserId(user.getId());

        assertEquals(1, groupPage.getGroups().size());
        GroupView view = groupPage.getGroups().get(0);
        assertTrue(view.getIsAdmin());
        assertEquals(group, view.getGroup());
        assertEquals(1, view.getMembers().size());
        Users member = view.getMembers().get(0);
        assertEquals(user, member);

        UserGroupLinksQueries.deleteUsersFromGroup(Collections.singletonList(user.getId()), group.getId());
        GroupsQueries.deleteGroup(group.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testUpdateGroupById() throws ServletException {
        givenUser();
        givenGroup();
        String newName = getRandomString(GROUP_NAME.length());
        String newDescription = getRandomString(GROUP_DESCRIPTION.length());

        Groups updatedGroup = groupServlet.updateGroupById(newName, newDescription, group.getId());

        assertEquals(newName, updatedGroup.getName());
        assertEquals(newDescription, updatedGroup.getDescription());
        GroupsQueries.deleteGroup(group.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testCreateGroup() throws ServletException {
        givenUser();
        OffsetDateTime time = OffsetDateTime.now();
        Users otherMember = givenUser(getRandomString(USER_EMAIL.length()).toLowerCase());
        GroupView view = groupServlet.createGroup(GROUP_NAME, GROUP_DESCRIPTION, time, user.getId(), otherMember.getEmail());
        group = new Groups(view.getGroup().getId(), GROUP_NAME, GROUP_DESCRIPTION, user.getId(), Timestamp.valueOf(time.toLocalDateTime()));

        assertTrue(view.getIsAdmin());
        assertEquals(group, view.getGroup());
        assertEquals(2, view.getMembers().size());
        assertTrue(view.getMembers().contains(user));
        assertTrue(view.getMembers().contains(otherMember));

        UserGroupLinksQueries.deleteUsersFromGroup(Arrays.asList(user.getId(), otherMember.getId()), group.getId());
        GroupsQueries.deleteGroup(group.getId());
        UsersQueries.deleteUserById(otherMember.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testDelete() throws ServletException {
        givenUser();
        givenGroup();

        int numDeleted = groupServlet.deleteGroupById(group.getId());

        assertEquals(1, numDeleted);
        UsersQueries.deleteUserById(user.getId());
    }

    private void givenGroup() throws ServletException {
        group = GroupsQueries.createGroup(GROUP_NAME, GROUP_DESCRIPTION, OffsetDateTime.now(), user.getId());
    }
}
