package main.java.queries;

import main.java.data.tables.pojos.Journals;
import main.java.util.Repository;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import java.sql.Time;
import java.sql.Timestamp;

import static main.java.data.Tables.JOURNALS;

public class JournalsQueries {
    public static void updateJournal(String entry, Timestamp timestamp, Journals journalRecord) throws ServletException{
        Repository.run((DSLContext r) ->
            r.update(JOURNALS)
             .set(JOURNALS.ENTRY, entry)
             .set(JOURNALS.TIMESTAMP, timestamp)
             .where(JOURNALS.ID.eq(journalRecord.getId()))
             .execute()
        );
    }
    public static void createJournal(String entry, Timestamp timestamp, int userId) throws ServletException{
        Repository.run((DSLContext r) ->
            r.insertInto(JOURNALS, JOURNALS.ENTRY, JOURNALS.TIMESTAMP, JOURNALS.USER_ID)
             .values(entry, timestamp, userId)
             .execute()
        );
    }
    public static Journals getJournalForUserBetweenTimestamps(int userId, Timestamp t0, Timestamp t1) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.selectFrom(JOURNALS)
             .where(JOURNALS.USER_ID.eq(userId))
             .and(JOURNALS.TIMESTAMP.ge(t0))
             .and(JOURNALS.TIMESTAMP.le(t1))
             .limit(1).fetchOneInto(Journals.class)
        );
    }
}
