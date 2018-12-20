package xyz.cetacea.queries;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Groups;

import javax.servlet.ServletException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupsQueriesTest extends QueriesTest {

    @BeforeEach()
    void setup() throws ServletException {
        givenCreatedUser();
        givenCreatedGroup();
    }

    @Test
    void testCreateGroup() {
        assertNotNull(createdGroup);
        verifyGroup(createdGroup);
    }

    @Test
    void testGetGroupInfoByGroupIds() throws ServletException {
        List<Groups> groupsInfo = GroupsQueries.getGroupInfoByGroupIds(Collections.singletonList(createdGroup.getId()));
        assertEquals(1, groupsInfo.size(), "Couldn't find group by Id: " + createdGroup.getId());
        verifyGroup(groupsInfo.get(0));
    }

    @Test
    void testGetAllGroups() throws ServletException {
        List<Groups> groupsInfo = GroupsQueries.getAllGroups();
        assertTrue(groupsInfo.contains(createdGroup), "Couldn't find group by all");
    }

    @Test
    void testUpdateGroup() throws ServletException {
        String newName = getRandomString(8);
        String newDescription = getRandomString(16);
        Groups updated = GroupsQueries.updateGroup(newName, newDescription, createdGroup.getId());
        assertNotNull(updated, "Failed to update group");
        assertEquals(newName, updated.getName());
        assertEquals(newDescription, updated.getDescription());
    }

    @Test
    void testDeleteGroup() throws ServletException {
        int numDeleted = GroupsQueries.deleteGroup(createdGroup.getId());
        assertEquals(1, numDeleted, "Failed to delete group");
    }

    @AfterEach
    void cleanup() throws ServletException {
        GroupsQueries.deleteGroup(createdGroup.getId());
        UsersQueries.deleteUserById(createdUser.getId());
    }

    private void verifyGroup(Groups group) {
        assertEquals(GROUP_NAME, group.getName());
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

}
