package xyz.cetacea.endpoints;

import org.eclipse.jetty.http.HttpMethod;
import xyz.cetacea.data.tables.pojos.*;
import xyz.cetacea.queries.JournalsQueries;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.util.Param;

import javax.annotation.Nullable;
import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Created by David on 9/25/2017.
 */
public class JournalServlet extends BaseServlet {

    @Endpoint(HttpMethod.GET)
    public Journals getTodayJournalByUserId(@Param("id") int userId) throws ServletException {
        Journals journal = getJournalById(userId);
        if (journal == null) {
            return new Journals();
        }
        return journal;
    }

    @Endpoint(HttpMethod.POST)
    public Journals getJournalByUserIdAndDate(@Param("id") int userId, @Param("date") OffsetDateTime day) throws ServletException {
        OffsetDateTime from = day.withHour(7).withMinute(0).withSecond(0);
        OffsetDateTime to = day.plusDays(1).withHour(7).withMinute(0).withSecond(0);
        Journals journal = getJournalById(userId, from, to);
        if (journal == null) {
            return new Journals();
        }
        return journal;
    }

    @Endpoint(HttpMethod.PUT)
    public Journals saveJournal(@Param("entry") String entry, @Param("timestamp") OffsetDateTime timeFromClient, @Param("user_id") int userId) throws ServletException {
        Timestamp timestamp = Timestamp.valueOf(timeFromClient.toLocalDateTime());
        Journals journalRecord = getJournalById(userId);
        if (journalRecord != null) {
            return JournalsQueries.updateJournal(entry, timestamp, journalRecord.getId());
        } else {
            return JournalsQueries.createJournal(entry, timestamp, userId);
        }
    }

    @Endpoint(HttpMethod.DELETE)
    public int deleteJournalById(@Param("id") int id) throws ServletException {
        return JournalsQueries.deleteJournal(id);
    }

    @Nullable
    private static Journals getJournalById(int userId) throws ServletException{
        OffsetDateTime[] dateRange = getDateRange();
        return getJournalById(userId, dateRange[0], dateRange[1]);
    }

    //TODO: Make private once Email Sender is its own service
    @Nullable
    public static Journals getJournalById(int userId, OffsetDateTime start, OffsetDateTime end) throws ServletException{
        System.out.println("Getting a journal for " + userId + " between " + start.toString() + " and " + end.toString());
        Timestamp t0 = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp t1 = Timestamp.valueOf(end.toLocalDateTime());
        return JournalsQueries.getJournalForUserBetweenTimestamps(userId, t0, t1);
    }

    private static OffsetDateTime[] getDateRange(){
        //TODO: Get range based on Group configuration (Daily, Weekly, etc...)
        LocalTime timeToSend = LocalTime.of(11, 0); //TODO: Get from group configuration
        OffsetDateTime[] dateRange = new OffsetDateTime[2];
        dateRange[1] = OffsetDateTime.now();
        dateRange[1] = dateRange[1].minusSeconds(dateRange[1].getOffset().getTotalSeconds());
        dateRange[0] = dateRange[1].withHour(timeToSend.getHour())
                                   .withMinute(timeToSend.getMinute())
                                   .withSecond(timeToSend.getSecond())
                                   .withNano(0);
        if (dateRange[0].compareTo(dateRange[1]) >= 0){
            dateRange[0] = dateRange[0].minusDays(1);
        }
        return dateRange;
    }
}
