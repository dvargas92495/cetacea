package main.java.endpoints;

import static main.java.data.Tables.*;

import jdk.nashorn.internal.scripts.JO;
import main.java.util.Repository;
import main.java.util.RequestHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 9/25/2017.
 */
public class JournalServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        int queryId = Integer.parseInt(request.getParameter("id"));
        Map<String, String> responseBody = getJournalById(queryId);
        RequestHelper.setResponseToMap(response, responseBody);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> params = RequestHelper.getBodyAsMap(request);
        DSLContext create = Repository.getContext();
        String entry = params.get("entry");
        OffsetDateTime timestamp = OffsetDateTime.parse(params.get("timestamp"));
        int userId = Integer.parseInt(params.get("user_id"));
        Map<String, String> resposeBody = getJournalById(userId);
        if (resposeBody.isEmpty()) {
            create.insertInto(JOURNALS, JOURNALS.ENTRY, JOURNALS.TIMESTAMP, JOURNALS.USER_ID)
                    .values(entry, timestamp, userId)
                    .execute();
        } else {
            int journalId = Integer.parseInt(resposeBody.get("id"));
            create.update(JOURNALS)
                    .set(JOURNALS.ENTRY, entry)
                    .set(JOURNALS.TIMESTAMP, timestamp)
                    .where(JOURNALS.ID.eq(journalId))
                    .execute();
        }
    }

    static ArrayList<String> getJournalsByEmails(ArrayList<String> emails) throws ServletException{
        ArrayList<String> journals = new ArrayList<String>();
        if (emails.size() == 0){
            return journals;
        }
        DSLContext query = Repository.getContext();
        ArrayList<Integer> userIds = new ArrayList<>();
        emails.forEach(e -> userIds.add(query.select()
                                             .from(USERS)
                                             .where(USERS.EMAIL.eq(e))
                                             .fetchOne()
                                             .get(USERS.ID)));
        userIds.forEach(u -> {
            try {
                Map<String, String> entry = JournalServlet.getJournalById(u);
                if (!entry.isEmpty()) {
                    journals.add(entry.get(JOURNALS.ENTRY.getName()));
                }
            } catch (ServletException ex) {
                ex.printStackTrace();
            }
        });
        return journals;
    }

    private static Map<String, String> getJournalById(int userId) throws ServletException{
        DSLContext create = Repository.getContext();

        OffsetDateTime[] dateRange = getDateRange();
        Result<Record> result = create.select()
                .from(JOURNALS)
                .where(JOURNALS.USER_ID.eq(userId))
                .and(JOURNALS.TIMESTAMP.ge(dateRange[0]))
                .and(JOURNALS.TIMESTAMP.le(dateRange[1]))
                .fetch();
        Map<String, String> journal = new HashMap<>();
        if (result.isNotEmpty()){
            Record journalRecord = result.get(result.size() - 1);
            RequestHelper.addFieldToMap(journal, JOURNALS.ID, journalRecord);
            RequestHelper.addFieldToMap(journal, JOURNALS.TIMESTAMP, journalRecord);
            RequestHelper.addFieldToMap(journal, JOURNALS.ENTRY, journalRecord);
            RequestHelper.addFieldToMap(journal, JOURNALS.USER_ID, journalRecord);
        }
        return journal;
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
