package xyz.cetacea.queries;

import xyz.cetacea.data.tables.pojos.Journals;
import xyz.cetacea.util.Repository;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import java.sql.Timestamp;

import static xyz.cetacea.data.Tables.JOURNALS;

public class JournalsQueries {

    public static Journals createJournal(String entry, Timestamp timestamp, int userId) throws ServletException{
        return Repository.run((DSLContext r) ->
                r.insertInto(JOURNALS, JOURNALS.ENTRY, JOURNALS.TIMESTAMP, JOURNALS.USER_ID)
                        .values(entry, timestamp, userId)
                        .returning().fetchOne().into(Journals.class)
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

    public static Journals updateJournal(String entry, Timestamp timestamp, int id) throws ServletException{
        return Repository.run((DSLContext r) ->
            r.update(JOURNALS)
             .set(JOURNALS.ENTRY, entry)
             .set(JOURNALS.TIMESTAMP, timestamp)
             .where(JOURNALS.ID.eq(id))
             .returning().fetchOne().into(Journals.class)
        );
    }

    public static int deleteJournal(int id) throws ServletException {
        return Repository.run((DSLContext r) ->
            r.deleteFrom(JOURNALS)
             .where(JOURNALS.ID.eq(id))
             .execute()
        );
    }
}
