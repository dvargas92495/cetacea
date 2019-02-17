package xyz.cetacea.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Journals;
import xyz.cetacea.queries.JournalsQueries;
import xyz.cetacea.queries.UsersQueries;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class JournalServletTest extends ServletTest {

    private static JournalServlet journalServlet;
    private static Journals journal;

    BaseServlet getServlet() { return journalServlet; }

    @BeforeEach
    void setup() {
        journalServlet = new JournalServlet();
    }

    @Test
    void testGet() throws ServletException {
        givenUser();
        givenJournal();

        Journals todayJournal = journalServlet.getTodayJournalByUserId(user.getId());

        assertEquals(journal, todayJournal);

        JournalsQueries.deleteJournal(journal.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testGetEmptyJournal() throws ServletException {
        givenUser();

        Journals todayJournal = journalServlet.getTodayJournalByUserId(user.getId());

        assertEquals(new Journals(), todayJournal);

        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testPost() throws ServletException {
        givenUser();
        givenJournal();

        Journals fetchedJournal = journalServlet.getJournalByUserIdAndDate(user.getId(), OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC));

        assertEquals(journal, fetchedJournal);

        JournalsQueries.deleteJournal(journal.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testPostEmptyJournal() throws ServletException {
        givenUser();
        givenJournal();

        Journals fetchedJournal = journalServlet.getJournalByUserIdAndDate(user.getId(), OffsetDateTime.now().minusDays(3));

        assertEquals(new Journals(), fetchedJournal);

        JournalsQueries.deleteJournal(journal.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testPutCreate() throws ServletException {
        givenUser();

        journal = journalServlet.saveJournal(ENTRY, OffsetDateTime.now(), user.getId());

        assertEquals(ENTRY, journal.getEntry());
        assertEquals(user.getId(), journal.getUserId());

        JournalsQueries.deleteJournal(journal.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testPutUpdate() throws ServletException {
        givenUser();
        givenJournal();

        String newEntry = getRandomString(32);
        Journals updatedJournal = journalServlet.saveJournal(newEntry, OffsetDateTime.now(), user.getId());

        assertEquals(newEntry, updatedJournal.getEntry());
        assertEquals(user.getId(), updatedJournal.getUserId());

        JournalsQueries.deleteJournal(journal.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testDelete() throws ServletException {
        givenUser();
        givenJournal();

        int numDeleted = journalServlet.deleteJournalById(journal.getId());

        assertEquals(1, numDeleted);

        JournalsQueries.deleteJournal(journal.getId());
        UsersQueries.deleteUserById(user.getId());
    }

    private void givenJournal() throws ServletException {
        journal = JournalsQueries.createJournal(ENTRY, Timestamp.valueOf(OffsetDateTime.now().minusHours(1).toLocalDateTime()), user.getId());
    }
}
