package xyz.cetacea.queries;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Journals;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JournalsQueriesTest extends QueriesTest {

    private static Journals createdJournal;

    @BeforeEach
    void setup() throws ServletException {
        givenCreatedUser();
        givenCreatedJournal();
    }

    @Test
    void testCreateJournal() {
        assertNotNull(createdJournal);
        assertEquals(ENTRY, createdJournal.getEntry());
        assertEquals(createdUser.getId(), createdJournal.getUserId());
        assertTrue(createdJournal.getId() > 0);
    }

    @Test
    void testGetJournalForUserBetweenTimestamps() throws ServletException {
        Timestamp before = Timestamp.valueOf(LocalDateTime.now().minusHours(12));
        Timestamp after = Timestamp.valueOf(LocalDateTime.now().plusHours(12));
        Journals journal = JournalsQueries.getJournalForUserBetweenTimestamps(createdUser.getId(), before, after);
        assertEquals(createdJournal, journal);
    }

    @Test
    void testUpdateJournal() throws ServletException{
        String newEntry = getRandomString(32);
        Journals updatedJournal = JournalsQueries.updateJournal(newEntry, Timestamp.valueOf(LocalDateTime.now()), createdJournal.getId());
        assertEquals(newEntry, updatedJournal.getEntry());
        assertEquals(createdJournal.getId(), updatedJournal.getId());
        assertNotEquals(createdJournal.getTimestamp(), updatedJournal.getTimestamp());
    }

    @Test
    void testDeleteJournal() throws ServletException {
        int numDeleted = JournalsQueries.deleteJournal(createdJournal.getId());
        assertEquals(1, numDeleted);
    }

    @AfterEach
    void cleanup() throws ServletException {
        JournalsQueries.deleteJournal(createdJournal.getId());
        UsersQueries.deleteUserById(createdUser.getId());
    }

    private void givenCreatedJournal() throws ServletException {
        createdJournal = JournalsQueries.createJournal(ENTRY, Timestamp.valueOf(LocalDateTime.now()), createdUser.getId());
    }
}
