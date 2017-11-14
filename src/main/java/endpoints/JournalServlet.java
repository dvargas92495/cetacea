package main.java.endpoints;

import static main.java.data.Tables.*;

import com.google.gson.Gson;
import main.java.data.tables.pojos.*;
import main.java.util.Repository;
import main.java.util.RequestHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 9/25/2017.
 */
public class JournalServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String idParam = request.getParameter("id");
        if (idParam == null) return;
        response.setStatus(HttpServletResponse.SC_OK);
        int queryId = Integer.parseInt(idParam);
        Journals journalRecord = getJournalById(queryId);
        if (journalRecord == null){
            journalRecord = new Journals();
        }
        response.getWriter().println(new Gson().toJson(journalRecord, Journals.class));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        String entry = params.get("entry");
        OffsetDateTime timestamp = OffsetDateTime.parse(params.get("timestamp"));
        int userId = Integer.parseInt(params.get("user_id"));
        Journals journalRecord = getJournalById(userId);
        if (journalRecord != null) {
            Repository.getDsl().update(JOURNALS)
                    .set(JOURNALS.ENTRY, entry)
                    .set(JOURNALS.TIMESTAMP, timestamp)
                    .where(JOURNALS.ID.eq(journalRecord.getId()))
                    .execute();
        } else {
            Repository.getDsl().insertInto(JOURNALS, JOURNALS.ENTRY, JOURNALS.TIMESTAMP, JOURNALS.USER_ID)
                    .values(entry, timestamp, userId)
                    .execute();
        }
    }

    static List<String> getJournalsByEmails(List<String> emails) throws ServletException{
        ArrayList<String> journals = new ArrayList<String>();
        if (emails.size() == 0){
            return journals;
        }
        List<Integer> userIds = Repository.getDsl().selectFrom(USERS)
                .where(USERS.EMAIL.in(emails))
                .fetch(USERS.ID);

        userIds.forEach(u -> {
            try {
                Journals journalRecord = JournalServlet.getJournalById(u);
                if (journalRecord != null) {
                    journals.add(journalRecord.getEntry());
                }
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        });
        return journals;
    }

    private static Journals getJournalById(int userId) throws ServletException{
        OffsetDateTime[] dateRange = getDateRange();
        return Repository.getDsl().selectFrom(JOURNALS)
                .where(JOURNALS.USER_ID.eq(userId))
                .and(JOURNALS.TIMESTAMP.ge(dateRange[0]))
                .and(JOURNALS.TIMESTAMP.le(dateRange[1]))
                .fetchOneInto(Journals.class);
    }

    private static OffsetDateTime[] getDateRange(){
        //TODO: Get range based on Group configuration (Daily, Weekly, etc...)
        LocalTime timeToSend = LocalTime.of(6, 0); //TODO: Get from group configuration
        OffsetDateTime[] dateRange = new OffsetDateTime[2];
        dateRange[1] = OffsetDateTime.now();
        dateRange[0] = dateRange[1].withHour(timeToSend.getHour())
                                   .withMinute(timeToSend.getMinute())
                                   .withSecond(timeToSend.getSecond());
        if (dateRange[0].compareTo(dateRange[1]) > 0){
            dateRange[0] = dateRange[0].minusDays(1);
        }
        return dateRange;
    }
}
