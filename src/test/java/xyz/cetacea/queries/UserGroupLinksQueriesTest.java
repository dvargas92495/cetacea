package xyz.cetacea.queries;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.UserGroupLinks;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserGroupLinksQueriesTest extends QueriesTest {

    private static UserGroupLinks createdUserGroupLink;

    @BeforeEach()
    void setup() throws ServletException {
        givenCreatedUser();
        givenCreatedGroup();
        givenCreatedUserGroupLink();
    }

    @Test
    void testAddUserToGroup() {
        assertNotNull(createdUserGroupLink);
        assertEquals(createdGroup.getId(), createdUserGroupLink.getGroupId());
        assertEquals(createdUser.getId(), createdUserGroupLink.getUserId());
        assertTrue(createdUserGroupLink.getIsAdmin());
        assertTrue(createdUserGroupLink.getId() > 0);
    }

    @Test
    void testGetUserGroupLinksByUserId() throws ServletException {
        List<UserGroupLinks> linksInfo = UserGroupLinksQueries.getUserGroupLinksByUserId(createdUser.getId());
        assertEquals(1, linksInfo.size());
        UserGroupLinks link = linksInfo.get(0);
        assertEquals(createdUserGroupLink, link);
    }

    @Test
    void testGetUserIdsByGroupId() throws ServletException {
        List<Integer> userIds = UserGroupLinksQueries.getUserIdsByGroupId(createdGroup.getId());
        assertTrue(userIds.contains(createdUser.getId()));
    }

    @Test
    void testDeleteUsersFromGroup() throws ServletException {
        int numDeleted = UserGroupLinksQueries.deleteUsersFromGroup(Collections.singletonList(createdUser.getId()), createdGroup.getId());
        assertEquals(1, numDeleted);
    }

    private void givenCreatedUserGroupLink() throws ServletException {
        createdUserGroupLink = UserGroupLinksQueries.addUserToGroup(createdUser.getId(), createdGroup.getId(), Timestamp.valueOf(LocalDateTime.now()), true);
    }

    @AfterEach
    void cleanup() throws ServletException {
        UserGroupLinksQueries.deleteUsersFromGroup(Collections.singletonList(createdUser.getId()), createdGroup.getId());
        GroupsQueries.deleteGroup(createdGroup.getId());
        UsersQueries.deleteUserById(createdUser.getId());
    }
}
